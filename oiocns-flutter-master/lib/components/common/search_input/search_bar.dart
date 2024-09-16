import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:orginone/components/common/dialogs/popup_widget.dart';
import 'package:orginone/components/common/icons/ximage.dart';
import 'package:orginone/components/common/images/image_widget.dart';
import 'package:orginone/components/common/list/index.dart';
import 'package:orginone/components/common/text/target_text.dart';
import 'package:orginone/components/common/text/text_tag.dart';
import 'package:orginone/config/theme/unified_style.dart';
import 'package:orginone/dart/base/common/commands.dart';
import 'package:orginone/dart/controller/index.dart';
import 'package:orginone/dart/core/chat/session.dart';
import 'package:orginone/dart/core/public/enums.dart';
import 'package:orginone/dart/core/work/task.dart';
import 'package:orginone/routers/pages.dart';
import 'package:orginone/routers/router_const.dart';
import 'package:orginone/utils/date_util.dart';
import 'package:orginone/utils/log/log_util.dart';
import 'package:badges/badges.dart' as badges;

class SearchBar<T> extends SearchDelegate {
  final HomeEnum homeEnum;

  final List<T> data;

  SearchBar(
      {required this.homeEnum,
      required this.data,
      super.searchFieldLabel = "请输入关键字"});

  List<T> searchData = [];

  @override
  List<Widget>? buildActions(BuildContext context) {
    return [
      IconButton(
        icon: const Icon(
          Icons.clear,
          color: Colors.black,
        ),
        onPressed: () {
          query = "";
        },
      ),
    ];
  }

  @override
  Widget? buildLeading(BuildContext context) {
    LogUtil.d('>>>>>>>>>>>>>buildActions');
    return BackButton(
      color: Colors.black,
      onPressed: () {
        close(context, null);
      },
    );
  }

  @override
  Widget buildResults(BuildContext context) {
    LogUtil.d('>>>>>>>>>>>>>buildResults');
    search();
    return _body();
  }

  @override
  Widget buildSuggestions(BuildContext context) {
    LogUtil.d('>>>>>>>>>>>>>buildSuggestions');
    search();
    return _body();
  }

  void search() {
    searchData.clear();
    if (query.isEmpty) {
      searchData.addAll(data);
      return;
    }

    for (var element in data) {
      switch (homeEnum) {
        case HomeEnum.chat:
          if ((element as ISession).chatdata.value.chatName.contains(query) ??
              false) {
            searchData.add(element);
          }
          break;
        case HomeEnum.work:
          if ((element as IWorkTask).taskdata.title!.contains(query)) {
            searchData.add(element);
          }
          break;
        case HomeEnum.store:
          // var recent = (element as RecentlyUseModel);
          // if (recent.type == StoreEnum.file.label) {
          //   if (recent.file!.name!.contains(query)) {
          //     searchData.add(element);
          //   }
          // } else {
          //   if (recent.thing!.id!.contains(query)) {
          //     searchData.add(element);
          //   }
          // }
          break;
        default:
      }
    }
  }

  Widget _body() {
    return ListWidget<T>(
      initDatas: searchData,
      getDatas: ([dynamic data]) {
        if (null == data) {
          return searchData ?? [];
        }
        return [];
      },
      // getAction: (dynamic data) {
      //   return Text(
      //     CustomDateUtil.getSessionTime(data.updateTime),
      //     style: XFonts.chatSMTimeTip,
      //     textAlign: TextAlign.right,
      //   );
      // },
      onTap: (dynamic data, List children) {
        LogUtil.d('>>>>>>======点击了列表项 ${data.name}');
        if (data is ISession) {
          RoutePages.jumpChatSession(data: data);
        } else if (data is IWorkTask) {
          RoutePages.jumpWorkInfo(work: data);
        }
      },
    );
  }

  Widget body() {
    return ListView.builder(
        scrollDirection: Axis.vertical,
        itemCount: searchData.length,
        itemBuilder: (BuildContext context, int index) {
          var item = searchData[index];
          switch (homeEnum) {
            case HomeEnum.chat:
              return ListItem(
                adapter: ListAdapter.chat(item as ISession),
              );
            case HomeEnum.work:
              return ListItem(adapter: ListAdapter.work(item as IWorkTask));
            case HomeEnum.store:
            // return ListItem(
            //     adapter: ListAdapter.store(item as RecentlyUseModel));
            default:
          }
          return Container();
        });
  }

  @override
  ThemeData appBarTheme(BuildContext context) {
    LogUtil.d('>>>>>>>>>>>>>appBarTheme');
    return super.appBarTheme(context).copyWith(
          appBarTheme: super.appBarTheme(context).appBarTheme.copyWith(
                elevation: 0.0,
              ),
        );
  }
}

@Deprecated("待废弃")
class ListItem extends StatelessWidget {
  final ListAdapter adapter;

  const ListItem({
    Key? key,
    required this.adapter,
  }) : super(key: key);

//   @override
//   State<StatefulWidget> createState() {
//     return _ListItemState();
//   }
// }

// class _ListItemState extends State<ListItem> {
  @override
  Widget build(BuildContext context) {
    return Obx(() {
      return PopupWidget(
        itemBuilder: (BuildContext context) {
          return adapter.popupMenuItems;
        },
        onTap: () {
          adapter.callback?.call();
        },
        onSelected: (key) {
          adapter.onSelected?.call(key);
        },
        child: Container(
          color: Colors.white,
          padding: EdgeInsets.symmetric(horizontal: 15.w),
          child: Container(
            padding: EdgeInsets.symmetric(vertical: 7.h),
            decoration: BoxDecoration(
                border: Border(
                    bottom:
                        BorderSide(color: Colors.grey.shade300, width: 0.4))),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.start,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                _avatarContainer,
                SizedBox(
                  width: 10.w,
                ),
                Expanded(child: _content),
              ],
            ),
          ),
        ),
      );
    });
  }

  Widget get _avatarContainer {
    var noRead = adapter.noReadCount;
    // LogUtil.d('>>>>>>======view $noRead');
    Widget child = ImageWidget(
      adapter.image,
      size: 65.w,
      iconColor: const Color(0xFF9498df),
      circular: adapter.circularAvatar,
    );
    if (noRead.isNotEmpty) {
      child = badges.Badge(
        ignorePointer: false,
        position: badges.BadgePosition.topEnd(top: -6, end: -8),
        badgeContent: Text(
          noRead.value,
          // "${noRead > 99 ? "99+" : noRead}",
          style: const TextStyle(
            color: Colors.white,
            fontSize: 10,
            letterSpacing: 1,
            wordSpacing: 2,
            height: 1,
          ),
        ),
        child: child,
      );
    }
    return child;
  }

  Widget get _content {
    var labels = <Widget>[];
    for (var item in adapter.labels) {
      if (item.isNotEmpty) {
        bool isTop = item == "置顶";

        Widget label;

        var style = TextStyle(
          color: isTop ? XColors.fontErrorColor : XColors.designBlue,
          fontSize: 14.sp,
        );
        if (adapter.isUserLabel) {
          label = Container(
            decoration: BoxDecoration(
              color: Colors.white,
              borderRadius: const BorderRadius.all(Radius.circular(10)),
              border: Border.all(color: XColors.tinyBlue),
            ),
            padding: const EdgeInsets.symmetric(vertical: 2, horizontal: 5),
            child: TargetText(userId: item, style: style),
          );
        } else {
          label = TextTag(
            item,
            bgColor: Colors.white,
            padding: const EdgeInsets.symmetric(vertical: 1, horizontal: 3),
            textStyle: style,
            borderColor: isTop ? XColors.fontErrorColor : XColors.tinyBlue,
          );
        }

        labels.add(label);
        labels.add(Padding(padding: EdgeInsets.only(left: 4.w)));
      }
    }
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Row(
          children: [
            Expanded(
              child: Text(
                adapter.title,
                style: TextStyle(
                  color: XColors.chatTitleColor,
                  fontWeight: FontWeight.w500,
                  fontSize: 24.sp,
                  overflow: TextOverflow.ellipsis,
                ),
                maxLines: 1,
              ),
            ),
            Text(
              CustomDateUtil.getSessionTime(adapter.dateTime),
              style: XFonts.chatSMTimeTip,
              textAlign: TextAlign.right,
            ),
          ],
        ),
        SizedBox(
          height: 3.h,
        ),
        Row(
          children: labels,
        ),
        SizedBox(
          height: 3.h,
        ),
        _showTxt(),
      ],
    );
  }

  Widget _showTxt() {
    return Text(
      adapter.content,
      style: TextStyle(
        color: XColors.chatHintColors,
        fontSize: 18.sp,
      ),
      textAlign: TextAlign.left,
      maxLines: 1,
      overflow: TextOverflow.ellipsis,
    );
  }
}

class ListAdapter {
  VoidCallback? callback;

  List<PopupMenuItem> popupMenuItems = [];

  late String title;

  late List<String> labels;

  dynamic image;

  late String content;

  late RxString noReadCount;

  String? dateTime;

  late bool circularAvatar;

  late bool isUserLabel;

  PopupMenuItemSelected? onSelected;

  String? typeName;

  ListAdapter({
    this.title = '',
    this.labels = const [],
    this.image,
    this.content = '',
    this.dateTime,
    this.isUserLabel = false,
    noReadCount,
    this.circularAvatar = false,
    this.callback,
    this.popupMenuItems = const [],
  }) {
    this.noReadCount = noReadCount ?? ''.obs;
  }

  ListAdapter.chat(ISession chat) {
    noReadCount = chat.noReadCount.obs;
    labels = chat.groupTags;
    bool isTop = labels.contains("常用");
    isUserLabel = false;
    typeName = chat.share.typeName;
    popupMenuItems = [
      PopupMenuItem(
        value: isTop ? PopupMenuKey.cancelTopping : PopupMenuKey.topping,
        child: Text(isTop ? "取消常用" : "设为常用"),
      ),
      const PopupMenuItem(
        value: PopupMenuKey.delete,
        child: Text("删除"),
      ),
    ];
    onSelected = (key) async {
      switch (key) {
        case PopupMenuKey.cancelTopping:
          chat.groupTags.remove('常用');
          chat.chatdata.value.isToping = false;
          await chat.cacheChatData();
          // relationCtrl.loadChats();
          break;
        case PopupMenuKey.topping:
          chat.groupTags.add('常用');
          chat.chatdata.value.isToping = true;
          await chat.cacheChatData();
          // relationCtrl.loadChats();
          break;
        case PopupMenuKey.delete:
          chat.chatdata.value.recently = false;
          await chat.cacheChatData();
          // relationCtrl.chats.remove(chat);
          // relationCtrl.loadChats();
          break;
      }
    };
    circularAvatar = chat.share.typeName == TargetType.person.label;
    initNoReadCommand(chat.chatdata.value.fullId, chat);
    title = chat.chatdata.value.chatName ?? "";
    dateTime = chat.updateTime;
    content = chat.remark;
    // var lastMessage = chat.chatdata.value.lastMessage;
    // if (lastMessage != null) {
    //   if (lastMessage.fromId != relationCtrl.user.metadata.id) {
    //     if (chat.share.typeName != TargetType.person.label) {
    //     } else {
    //       content = "对方:";
    //     }
    //   }
    //   content = content + chat.remark;
    //   // StringUtil.msgConversion(lastMessage, relationCtrl.user.userId);
    // }

    image = chat.share.avatar?.thumbnailUint8List ??
        chat.share.avatar?.defaultAvatar;

    callback = () async {
      // chat.onMessage((messages) => null);
      await Get.toNamed(
        Routers.messageChat,
        arguments: chat,
      );
    };
  }
  ListAdapter.work(IWorkTask work) {
    noReadCount = ''.obs;
    labels = work.groupTags;
    // labels = [
    //   work.creater.name,
    //   work.taskdata.taskType!,
    //   work.taskdata.approveType!
    // ];
    isUserLabel = false;
    circularAvatar = false;
    title = work.taskdata.title ?? '';
    dateTime = work.metadata.createTime ?? "";
    content = work.taskdata.content ?? "";

    // LogUtil.d('ShareIdSet');
    // LogUtil.d(ShareIdSet[work.metadata.id]?.avatar);
    // image = ShareIdSet[work.metadata.id]?.avatar?.thumbnailUint8List ??
    //     AssetsImages.iconWorkitem;
    image = XImage.entityIcon(
        work); //IconsUtils.workDefaultAvatar(work.taskdata.taskType ?? '');
    if (work.targets.length == 2) {
      content =
          "${work.targets[0].name}[${work.targets[0].typeName}]申请加入${work.targets[1].name}[${work.targets[1].typeName}]";
    }

    content = content.isEmpty ? '暂无信息' : "内容:$content";

    ///点击回调
    callback = () async {
      if (work.targets.isEmpty) {
        //加载流程实例数据
        await work.loadInstance();
      }
      //跳转办事详情
      Get.toNamed(Routers.processDetails, arguments: {"todo": work});
    };
  }

  // ListAdapter.application(IApplication application, ITarget target) {
  //   noReadCount = ''.obs;
  //   labels = [target.name ?? ""];
  //   isUserLabel = false;
  //   circularAvatar = false;
  //   title = application.name ?? "";
  //   dateTime = application.metadata.createTime ?? "";
  //   content = "应用说明:${application.metadata.remark ?? ""}";
  //   image = application.metadata.avatarThumbnail() ?? Ionicons.apps;

  //   callback = () async {
  //     var works = await application.loadWorks();
  //     var nav = GeneralBreadcrumbNav(
  //         id: application.metadata.id ?? "",
  //         name: application.metadata.name ?? "",
  //         source: application,
  //         spaceEnum: SpaceEnum.applications,
  //         space: target,
  //         children: [
  //           ...works.map((e) {
  //             return GeneralBreadcrumbNav(
  //               id: e.metadata.id ?? "",
  //               name: e.metadata.name ?? "",
  //               spaceEnum: SpaceEnum.work,
  //               space: target,
  //               source: e,
  //               children: [],
  //             );
  //           }).toList(),
  //           ..._loadModuleNav(application.children, target),
  //         ]);
  //     Get.toNamed(Routers.generalBreadCrumbs, arguments: {"data": nav});
  //   };
  // }

  // ListAdapter.store(RecentlyUseModel recent) {
  //   noReadCount = ''.obs;
  //   image = recent.avatar ?? Ionicons.clipboard_sharp;
  //   labels = [recent.thing == null ? "文件" : "物"];
  //   callback = () {
  //     if (recent.file != null) {
  //       RoutePages.jumpFile(file: recent.file!, type: "store");
  //     }
  //   };
  //   title = recent.thing?.id ?? recent.file?.name ?? "";
  //   isUserLabel = false;
  //   circularAvatar = false;
  //   content = '';
  //   dateTime = recent.createTime;
  // }

  // List<GeneralBreadcrumbNav> _loadModuleNav(
  //     List<IApplication> app, ITarget target) {
  //   List<GeneralBreadcrumbNav> navs = [];
  //   for (var value in app) {
  //     navs.add(GeneralBreadcrumbNav(
  //         id: value.metadata.id ?? "",
  //         name: value.metadata.name ?? "",
  //         source: value,
  //         spaceEnum: SpaceEnum.module,
  //         space: target,
  //         onNext: (item) async {
  //           var works = await value.loadWorks();
  //           List<GeneralBreadcrumbNav> data = [
  //             ...works.map((e) {
  //               return GeneralBreadcrumbNav(
  //                 id: e.metadata.id ?? "",
  //                 name: e.metadata.name ?? "",
  //                 spaceEnum: SpaceEnum.work,
  //                 source: e,
  //                 space: target,
  //                 children: [],
  //               );
  //             }),
  //             ..._loadModuleNav(value.children, target),
  //           ];
  //           item.children = data;
  //         },
  //         children: []));
  //   }
  //   return navs;
  // }

  void initNoReadCommand(String key, ISession chat) {
    if (chat.chatdata.value.noReadCount > 0) {
      noReadCount.value = chat.chatdata.value.noReadCount > 99
          ? "99+"
          : chat.chatdata.value.noReadCount.toString();
    } else {
      noReadCount.value = '';
    }
    //沟通未读消息提示处理
    command.subscribeByFlag('session-$key',
        ([List<dynamic>? args]) => {refreshNoReadMgsCount(args)});
  }

  void refreshNoReadMgsCount([List<dynamic>? args]) {
    if (null != args && args.isNotEmpty) {
      // LogUtil.d('>>>>>>======start ${noReadCount.value}');
      noReadCount.value = args[0] > 0 ? args[0].toString() : '';
      // LogUtil.d('>>>>>>======end ${noReadCount.value}');
    }
  }
}
