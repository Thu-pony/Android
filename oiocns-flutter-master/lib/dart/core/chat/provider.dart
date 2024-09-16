// 沟通接口
import 'package:common_utils/common_utils.dart' hide LogUtil;
import 'package:orginone/dart/base/common/commands.dart';
import 'package:orginone/dart/base/common/emitter.dart';
import 'package:orginone/dart/core/chat/activity.dart';
import 'package:orginone/dart/core/chat/session.dart';
import 'package:orginone/dart/core/target/person.dart';
import 'package:orginone/utils/log/log_util.dart';

///沟通起拱器接口
abstract class IChatProvider with EmitterMixin {
  ///会话数据
  late List<ISession> chats;

  /// 沟通未读消息数
  late int noReadChatCount;

  /// 动态处理器
  late IActivityProvider activityProvider;

  ///加载
  Future<void> load({bool reload = false});
}

///沟通起拱器
class ChatProvider with EmitterMixin implements IChatProvider {
  /// 用户信息
  final IPerson user;

  ///会话数据
  @override
  late List<ISession> chats;

  /// 沟通未读消息数
  @override
  late int noReadChatCount;

  /// 动态处理器
  @override
  late IActivityProvider activityProvider;

  /// 是否初始化
  bool _inited = false;

  ChatProvider(this.user) {
    noReadChatCount = 0;
    chats = [];
    activityProvider = ActivityProvider(user, this);
    addDataListener();
  }

  ///添加数据监听
  void addDataListener() {
    //有新会话消息通知
    command.subscribeByFlag("session", ([List<dynamic>? args]) {
      if (args != null && args.isNotEmpty) {
        var arg = args.first;
        if (arg is Map && arg.containsKey('operate_change')) {
          LogUtil.d(
              'subscribeByFlag--收到operate_change  ------ ${DateUtil.getNowDateMs()}');
          load(reload: true);
          return;
        }
      }
      //TODO 有新沟通消息通知
      chats = _filterAndSortChats(chats);
      _refreshNoReadMgsCount();
      changeCallback();
    });
  }

  ///加载
  @override
  Future<void> load({bool reload = false}) async {
    List<ISession> tmpChats = [];
    if (!_inited || reload) {
      tmpChats.addAll(user.chats ?? []);
      for (var company in user.companys ?? []) {
        tmpChats.addAll(company.chats ?? []);
      }
      // LogUtil.e('load  ------ ${DateUtil.getNowDateMs()}');
      // LogUtil.e('user.chats  ------');
      // LogUtil.e(user.chats
      //     .map((e) => {
      //           'name': e.name,
      //           'isMyChat': e.isMyChat,
      //           'lastMessage': e.chatdata.value.lastMessage?.content,
      //           'recently': e.chatdata.value.recently,
      //           'noReadCount': e.chatdata.value.noReadCount,
      //         })
      //     .toList());
      // LogUtil.e('chats  ------ ${DateUtil.getNowDateMs()}');
      chats = _filterAndSortChats(tmpChats);
      _refreshNoReadMgsCount();
      await activityProvider.load(reload: reload);
      changeCallback();
      _inited = true;
    }
  }

  /// 过滤和排序消息
  List<ISession> _filterAndSortChats(List<ISession> sessions) {
    //过滤出最近和有发过消息的会话
    sessions = sessions
        .where((element) =>
            element.isMyChat &&
            (element.chatdata.value.lastMessage != null ||
                element.chatdata.value.recently))
        .toList();

    /// 排序
    sessions.sort((a, b) {
      var num = 0;
      if (b.chatdata.value.lastMsgTime == a.chatdata.value.lastMsgTime) {
        num = b.isBelongPerson ? 1 : -1;
      } else {
        num = b.chatdata.value.lastMsgTime > a.chatdata.value.lastMsgTime
            ? 5
            : -5;
      }
      return num;
    });
    return sessions;
  }

  void _refreshNoReadMgsCount() {
    if (chats.isNotEmpty) {
      if (noReadChatCount != 0) {
        noReadChatCount = 0;
      }
      for (var element in chats) {
        noReadChatCount += element.chatdata.value.noReadCount;
      }
    }
  }
}

///动态提供器接口
abstract class IActivityProvider with EmitterMixin {
  ///群动态
  late GroupActivity cohortActivity;

  ///好友圈
  late GroupActivity friendsActivity;
  //加载
  Future<void> load({bool reload = false});
}

///动态提供器接口
class ActivityProvider with EmitterMixin implements IActivityProvider {
  final IPerson user;

  ///沟通提供器
  final IChatProvider chatProvider;

  ///群动态
  @override
  late GroupActivity cohortActivity;

  ///好友圈
  @override
  late GroupActivity friendsActivity;
  ActivityProvider(this.user, this.chatProvider) {
    cohortActivity = GroupActivity(
      user,
      () {
        return chatProvider.chats
            .where((i) => i.isMyChat && i.isGroup)
            .map((i) => i.activity)
            .toList();
      },
      false,
    );
    friendsActivity = GroupActivity(
      user,
      () {
        return [
          user.session.activity,
          ...user.memberChats.map((i) => i.activity).toList()
        ];
      },
      true,
    );
  }

  //加载
  @override
  Future<void> load({bool reload = false}) async {
    await cohortActivity.load(reload: reload);
    await friendsActivity.load(reload: reload);
    changeCallback();
  }
}
