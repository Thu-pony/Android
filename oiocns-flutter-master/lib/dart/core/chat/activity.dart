import 'dart:convert';
import 'dart:math';

import 'package:common_utils/common_utils.dart';
import 'package:get/get.dart';
import 'package:orginone/components/common/icons/ximage.dart';
import 'package:orginone/dart/base/common/emitter.dart';
import 'package:orginone/dart/base/model.dart';
import 'package:orginone/dart/base/schema.dart';
import 'package:orginone/dart/core/public/entity.dart';
import 'package:orginone/dart/core/public/collection.dart';
import 'package:orginone/dart/core/public/enums.dart';
import 'package:orginone/dart/core/chat/session.dart';
import 'package:orginone/dart/core/target/person.dart';

/// 动态集合名
const String activityCollName = '-resource-activity';

/// 动态消息接口
abstract class IActivityMessage with EmitterMixin, IBaseData {
  /// 唯一标识
  late String key;

  /// 消息主体
  late IActivity activity;

  /// 消息实体
  late ActivityType metadata;

  /// 是否可以删除
  late bool canDelete;

  /// 创建时间
  @override
  late int createTime;

  @override
  String get id;

  String get name;

  ShareIcon get share;
  String get typeName;

  /// 更新元数据
  void update(ActivityType data);

  /// 删除消息
  Future<void> delete();

  /// 点赞
  Future<bool> like();

  /// 评论
  Future<bool> comment(String txt, {String? replyTo});

  /// 转发
  Future<bool> forward(ISession target);
}

/// 动态消息实现
class ActivityMessage with EmitterMixin implements IActivityMessage {
  @override
  final IActivity activity;
  @override
  ActivityType metadata;

  ActivityMessage(
    this.metadata,
    this.activity,
  );
  @override
  String get id => activity.id;
  @override
  String get name => activity.name;
  @override
  ShareIcon get share => activity.share;
  @override
  String get typeName => activity.typeName;

  @override
  int get createTime {
    return DateUtil.getDateTime(metadata.createTime!)!.millisecondsSinceEpoch;
  }

  @override
  bool get canDelete {
    return (metadata.createUser == activity.userId ||
        (activity.session.sessionId == activity.session.target.id &&
            activity.session.target.hasRelationAuth()));
  }

  @override
  void update(ActivityType data) {
    if (data.id == metadata.id) {
      metadata = data;
      changeCallback();
    }
  }

  @override
  Future<void> delete() async {
    if (canDelete && (await activity.coll.delete(metadata))) {
      await activity.coll.notity({
        'data': metadata,
        'operate': 'delete',
      });
    }
    changeCallback();
  }

  @override
  Future<bool> like() async {
    bool res = false;
    ActivityType? newData;
    if (metadata.likes.contains(activity.userId)) {
      // metadata.likes.remove(activity.userId);
      newData = await activity.coll.update(
          metadata.id,
          {
            '_pull_': {'likes': activity.userId},
          },
          null,
          ActivityType.fromJson);
    } else {
      // metadata.likes.add(activity.userId);
      newData = await activity.coll.update(
          metadata.id,
          {
            '_push_': {'likes': activity.userId},
          },
          null,
          ActivityType.fromJson);
    }
    if (newData != null) {
      res = await activity.coll.notity({
        'data': newData,
        'operate': 'update',
      });
    }
    // changeCallback();
    return res;
  }

  @override
  Future<bool> comment(String label, {String? replyTo}) async {
    bool res = false;
    final newData = await activity.coll.update(
        metadata.id,
        {
          '_push_': {
            'comments': {
              'label': label,
              'userId': activity.userId,
              'time': 'sysdate()',
              'replyTo': replyTo,
            },
          },
        },
        null,
        ActivityType.fromJson);
    if (newData != null) {
      res = await activity.coll.notity({
        'data': newData,
        'operate': 'update',
      });
    }
    changeCallback();
    return res;
  }

  ///转发
  @override
  Future<bool> forward(ISession session) async {
    await session
        .sendMessage(MessageType.activity, jsonEncode(metadata.toJson()), []);
    return true;
  }

  @override
  dynamic noSuchMethod(Invocation invocation) => super.noSuchMethod(invocation);
}

/// 动态接口类
abstract class IActivity extends IEntity<XTarget> {
  /// 会话对象
  late ISession session;

  /// 是否允许发布
  late bool allPublish;

  /// 相关动态接口
  List<IActivity> get activitys;

  /// 当前查看的动态索引
  late int currIndex;

  /// 动态数据
  List<IActivityMessage> get activityList;

  ///动态集合
  XCollection<ActivityType> get coll;

  /// 发布动态
  Future<bool> send(
    String content,
    MessageType typeName,
    List<FileItemShare> resources,
    List<String> tags,
  );

  /// 加载动态
  Future<List<IActivityMessage>> load({bool reload = false, int take = 10});
}

/// 动态实现
class Activity extends Entity<XTarget> implements IActivity {
  @override
  final ISession session;

  @override
  late List<IActivityMessage> activityList;
  @override
  late XCollection<ActivityType> coll;
  bool finished = false;
  @override
  late int currIndex;

  Activity(metadata, this.session) : super(metadata, ['动态']) {
    activityList = [];
    if (session.target.id == session.sessionId) {
      coll = session.target.resource.genColl(activityCollName);
    } else {
      coll = XCollection<ActivityType>(
        metadata,
        activityCollName,
        [metadata.id],
        [key],
      );
    }
    currIndex = -1;
    subscribeNotify();
  }

  @override
  bool get allPublish {
    return (session.target.id == session.sessionId &&
        session.target.hasRelationAuth());
  }

  @override
  List<IActivity> get activitys {
    return [this];
  }

  @override
  Future<List<IActivityMessage>> load(
      {bool reload = false, int take = 10}) async {
    if (reload) {
      finished = false;
      activityList.clear();
    }
    if (!finished) {
      var data = await coll.load({
        'skip': activityList.length,
        'take': take,
        "options": {
          "match": Map<String, dynamic>.from({
            "isDeleted": false,
          }),
          "sort": {
            "createTime": -1,
          },
        },
      }, ActivityType.fromJson);
      final messages = data.map((i) => ActivityMessage(i, this));
      finished = messages.length < take;
      activityList.addAll(messages);
      return activityList;
    }
    return [];
  }

  @override
  Future<bool> send(
    String content,
    MessageType typeName,
    List<FileItemShare> resources,
    List<String> tags,
  ) async {
    if (allPublish) {
      var data = await coll.insert(
          ActivityType(
            tags: tags,
            comments: [],
            content: content,
            resource: resources,
            typeName: typeName.label,
            likes: [],
            forward: [],
            id: '',
          ),
          fromJson: ActivityType.fromJson);
      if (data != null) {
        await coll.notity({
          'data': data,
          'operate': 'insert',
        }, onlineOnly: false);
      }
      return data != null;
    }
    return false;
  }

  subscribeNotify() {
    coll.subscribe(
      [key],
      (data) {
        ActivityType res = ActivityType.fromJson(data['data']);
        switch (data['operate']) {
          case 'insert':
            activityList.insert(
              0,
              ActivityMessage(res, this),
            );
            changeCallback(args: [data, res]);
            break;
          case 'update':
            {
              var index = activityList.indexWhere(
                (i) => i.metadata.id == res.id,
              );
              if (index > -1) {
                activityList[index].update(res);
              }
            }
            changeCallback(args: [data, res]);
            break;
          case 'delete':
            activityList = activityList
                .where(
                  (i) => i.metadata.id != res.id,
                )
                .toList();
            changeCallback(args: [data, res]);
            break;
        }
      },
    );
  }

  @override
  dynamic noSuchMethod(Invocation invocation) => super.noSuchMethod(invocation);
}

class GroupActivity extends Entity<XTarget>
    with MDataSort<IActivityMessage>
    implements IActivity {
  @override
  late ISession session;
  @override
  late bool allPublish;
  List<String> subscribeIds = [];
  Map<String, String> subscribeSubActivityIds = {};
  late List<IActivity> Function() _loadActivity;
  late List<IActivity> subActivitys;
  int lastTime = DateTime.now().millisecondsSinceEpoch;
  late List<IActivityMessage> _activityList;
  @override
  late int currIndex;

  /// 是否初始化完成
  late bool _inited;

  GroupActivity(
      IPerson _user, List<IActivity> Function() loadActivity, bool userPublish)
      : super(
          XTarget.fromJson({
            ..._user.metadata.toJson(),
            'name': '全部',
            'typeName': '动态',
            'icon': XImage.dynamicIcon,
            'id': '${_user.id}xxx',
          }),
          ['全部动态'],
        ) {
    allPublish = userPublish;
    session = _user.session;
    _loadActivity = loadActivity;
    _activityList = [];
    _inited = false;
    currIndex = -1;
    // load();
    subActivitys = [];
    // if (allPublish) {
    //   subscribeNotify();
    // }
  }

  @override
  List<IActivity> get activitys {
    return [this, ...subActivitys];
  }

  @override
  XCollection<ActivityType> get coll {
    return session.activity.coll;
  }

  @override
  List<IActivityMessage> get activityList => _activityList;
  // {
  //   List<IActivityMessage> more = [];
  //   for (var activity in subActivitys) {
  //     more.addAll(activity.activityList
  //         .where((i) => i.createTime >= lastTime)
  //         .toList());
  //   }
  //   more.sort((a, b) => b.createTime - a.createTime);
  //   return more;
  // }

  @override
  Future<List<IActivityMessage>> load(
      {bool reload = false, int take = 10}) async {
    if (reload) {
      _clear();
    } else if (_inited) {
      return _activityList;
    }
    _loadSubActivity(reload: reload);

    List<IActivityMessage> more = [];
    await Future.wait(
        subActivitys.map((i) => i.load(reload: reload, take: take)));
    for (var activity in subActivitys) {
      more.addAll(activity.activityList.where((i) => i.createTime < lastTime));
    }
    sort(more);
    var news = more.getRange(0, min(more.length, take)).toList();
    if (news.isNotEmpty) {
      lastTime = news[news.length - 1].createTime;
      _activityList.addAll(news);
    } else {
      _inited = true;
    }
    return news;
  }

  void _loadSubActivity({bool reload = false}) {
    if (subActivitys.isEmpty) {
      subActivitys = _loadActivity();
      _subscribeNotifySubActivitys();
    } else {
      List<IActivity> newActivitys = [];
      List<IActivity> delActivitys = [];
      List<IActivity> tmpActivitys = _loadActivity();

      for (var element in tmpActivitys) {
        IActivity? newActivity =
            subActivitys.firstWhereOrNull((e) => e.id == element.id);
        if (null == newActivity) {
          newActivitys.add(element);
        }
      }
      for (var element in subActivitys) {
        IActivity? delActivity =
            tmpActivitys.firstWhereOrNull((e) => e.id == element.id);
        if (null == delActivity) {
          delActivitys.add(element);
        }
      }
      if (newActivitys.isNotEmpty) subActivitys.addAll(newActivitys);
      if (delActivitys.isNotEmpty) {
        for (var element in delActivitys) {
          subActivitys.remove(element);
        }
      }
    }
  }

  void _clear() {
    lastTime = DateTime.now().millisecondsSinceEpoch;
    _activityList.clear();
    // subActivitys.clear();
    _inited = false;
  }

  @override
  Future<bool> send(
    String content,
    MessageType typeName,
    List<FileItemShare> resources,
    List<String> tags,
  ) {
    return session.activity.send(content, typeName, resources, tags);
  }

  void _subscribeNotifySubActivitys() {
    for (var activity in subActivitys) {
      _subscribeNotifySubActivity(activity);
    }
  }

  void _subscribeNotifySubActivity(IActivity activity) {
    activity.unsubscribe(subscribeSubActivityIds[activity.id]);
    subscribeSubActivityIds[activity.id] = activity.subscribe((k, args) {
      if (null != args && args.isNotEmpty && args.length > 1) {
        _notify(args[0], args[1], activity);
      }
    });
  }

  ///订阅推送
  void subscribeNotify() {
    coll.subscribe(
      [key],
      (data) {
        _notify(data);
      },
    );
  }

  ///推送处理
  void _notify(Map<String, dynamic> data,
      [ActivityType? resP, IActivity? activity]) {
    ActivityType res = resP ?? ActivityType.fromJson(data['data']);
    switch (data['operate']) {
      case 'insert':
        activityList.insert(
          0,
          ActivityMessage(res, activity ?? session.activity),
        );
        changeCallback(args: [data, res]);
        break;
      case 'update':
        {
          var index = activityList.indexWhere(
            (i) => i.metadata.id == res.id,
          );
          if (index > -1) {
            activityList[index].update(res);
          }
        }
        changeCallback(args: [data, res]);
        break;
      case 'delete':
        _activityList = activityList
            .where(
              (i) => i.metadata.id != res.id,
            )
            .toList();
        changeCallback(args: [data, res]);
        break;
    }
  }

  @override
  String subscribe(void Function(String key, List<dynamic>? args) callback,
      [bool? target = true]) {
    // for (var activity in subActivitys) {
    //   subscribeIds.add(activity.subscribe(callback, false));
    // }
    return super.subscribe(callback);
  }

  @override
  void unsubscribe([dynamic id]) {
    super.unsubscribe(id);
    // for (var activity in subActivitys) {
    //   activity.unsubscribe(subscribeIds);
    // }
  }
}

mixin MDataPreview<T> {
  // int currIndex(T curr);

  ///下一个数据
  T? next(T curr);

  ///上一个数据
  T? previous(T curr);
}

abstract class IBaseData {
  get id;
  get createTime;
}

mixin MDataSort<T extends IBaseData> {
  sort(List<T> data) {
    data.sort((a, b) => b.createTime - a.createTime);
  }
}
