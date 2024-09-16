import 'dart:isolate';
import 'dart:math';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:orginone/components/common/button/bottom_button_common.dart';
import 'package:orginone/components/common/button_floating/action_container.dart';
import 'package:orginone/components/common/dialogs/bottom_sheet_dialog.dart';
import 'package:orginone/components/common/dialogs/dialog.dart';
import 'package:orginone/components/common/icons/icon.dart';
import 'package:orginone/components/common/icons/ximage.dart';
import 'package:orginone/components/common/list/index.dart';
import 'package:orginone/components/common/tab_pages/infoTabsController.dart';
import 'package:orginone/components/common/tab_pages/types.dart';
import 'package:orginone/components/common/templates/orginone_page_widget.dart';
import 'package:orginone/components/common/tip/toast.dart';
import 'package:orginone/components/data_preview/chat/message_chat.dart';
import 'package:orginone/config/theme/unified_style.dart';
import 'package:orginone/dart/base/schema.dart';
import 'package:orginone/dart/core/chat/session.dart';
import 'package:orginone/dart/core/public/enums.dart';
import 'package:orginone/dart/core/target/base/team.dart';
import 'package:orginone/dart/core/target/innerTeam/department.dart';
import 'package:orginone/dart/core/target/outTeam/cohort.dart';
import 'package:orginone/dart/core/target/outTeam/group.dart';
import 'package:orginone/dart/core/target/person.dart';
import 'package:orginone/dart/core/target/team/company.dart';
import 'package:orginone/main_base.dart';
import 'package:orginone/routers/pages.dart';
import 'package:orginone/utils/log/log_util.dart';

/// 好友/成员列表页面
class MemberListPage extends OrginoneStatefulWidget {
  MemberListPage({super.key, super.data});

  @override
  State<StatefulWidget> createState() => _MemberListPageState();
}

class _MemberListPageState
    extends OrginoneStatefulState<MemberListPage, dynamic> {
  late final String _emitterKey;

  @override
  initState() {
    super.initState();
    if (widget.data is ITeam) {
      ITeam team = widget.data;
      _emitterKey = team.subscribe((key, args) {
        if (mounted) {
          setState(() {
            //刷新页面
          });
        }
      }, false);
    }
  }

  @override
  void dispose() {
    super.dispose();
    if (widget.data is ITeam) {
      ITeam team = widget.data;
      team.unsubscribe(_emitterKey);
    }
  }

  @override
  Widget buildWidget(BuildContext context, dynamic data) {
    List<XTarget> members = [];
    if (data is IPerson) {
      members = loadMembers(data);
    } else if (data is ICompany) {
      members = loadMembers(data);
    } else if (data is IDepartment) {
      members = loadMembers(data);
    } else if (data is ICohort) {
      members = loadMembers(data);
    } else if (data is IGroup) {
      members = loadMembers(data);
    } else if (data is ISession) {
      members = loadMembers(data);
    }
    Widget content = Container(
        margin: EdgeInsets.symmetric(vertical: 1.h),
        decoration: const BoxDecoration(color: Colors.white),
        child: _buildList(context, members));
    if (hasAuth(data)) {
      content = GyPageWithBottomBtn(
        body: content,
        listAction: [
          ActionModel(
              title: isSelf ? "添加好友" : "邀请成员",
              onTap: () {
                if (isSelf) {
                  showSearchBottomSheet(Get.context!, TargetType.person,
                      title: "添加好友",
                      hint: "请输入用户的账号", onSelected: (targets) async {
                    bool success = false;
                    if (targets.isNotEmpty) {
                      success = await relationCtrl.user!.applyJoin(targets);
                      if (success) {
                        ToastUtils.showMsg(msg: "发送申请成功");
                      }
                    }
                  });
                } else {
                  showSearchBottomSheet(context, TargetType.person,
                      title: "邀请成员",
                      hint: "请输入用户的账号", onSelected: (targets) async {
                    bool success = false;
                    dynamic dataTargets = data.target;
                    if (targets.isNotEmpty) {
                      success = await dataTargets.pullMembers(targets);
                      if (success) {
                        setState(() {});
                      }
                    }
                  });
                }
              }),
        ],
      );
      // content = ActionContainer(
      //     floatingActionButton: FloatingActionButton(
      //       onPressed: () {
      //         showSearchBottomSheet(context, TargetType.person,
      //             title: "添加好友", hint: "请输入用户的账号", onSelected: (targets) async {
      //           bool success = false;
      //           if (targets.isNotEmpty && data is ITeam) {
      //             success = await data.pullMembers(targets);
      //             if (success) {
      //               setState(() {});
      //             }
      //           }
      //         });
      //       },
      //       mini: true,
      //       tooltip: "添加好友",
      //       child: const Icon(Icons.add),
      //     ),
      //     child: content);
    }
    return content;
  }

  bool hasAuth(dynamic data) {
    bool res = false;
    if (data is ISession) {
      data = data.target;
    }
    if (data is ITeam && data.hasRelationAuth() && data.id != data.userId) {
      res = true;
    }
    if (isSelf) {
      res = true;
    }

    return res;
  }

  bool get isSelf {
    return data.id == relationCtrl.user?.id;
  }

  // List<XTarget> loadFriends(IPerson user) {
  //   return user.members;
  // }

  List<XTarget> loadMembers(dynamic company) {
    return company.members;
  }

  Widget _buildConfirmDialog(BuildContext context, Function confirmFun,
      {String userName = "成员"}) {
    return CupertinoAlertDialog(
      title: Text("确认移除$userName？"),
      content: const Text(""),
      actions: <Widget>[
        CupertinoDialogAction(
          child: const Text('取消'),
          onPressed: () {
            Navigator.pop(context);
          },
        ),
        CupertinoDialogAction(
          child: const Text('确认'),
          onPressed: () {
            confirmFun();
          },
        ),
      ],
    );
  }

  _removeFriend(dynamic dataitem) {
    return buildBottomModal(
        context,
        [
          ActionModel(
              title: "移除好友",
              onTap: () async {
                Get.dialog(
                    Center(
                      child: Material(
                          color: Colors.transparent,
                          child: _buildConfirmDialog(context, () async {
                            if (dataitem is XTarget) {
                              bool success = await relationCtrl.user!
                                  .removeMembers([dataitem]);
                              if (success) {
                                ToastUtils.showMsg(msg: "移除好友成功");
                                setState(() {});
                              } else {
                                ToastUtils.showMsg(msg: "移除失败，请稍后重试");
                              }
                              Get.back();
                              Get.back();
                            }
                          }, userName: dataitem.name ?? "成员")),
                    ),
                    barrierColor: Colors.black.withOpacity(0.6));
              })
        ],
        isNeedPop: false);
  }

  _removeMember(dynamic dataitem) {
    return buildBottomModal(
        context,
        [
          ActionModel(
              title: "移除成员",
              onTap: () async {
                Get.dialog(
                    Center(
                      child: Material(
                          color: Colors.transparent,
                          child: _buildConfirmDialog(context, () async {
                            dynamic dataTargets = data.target;
                            if (dataitem is XTarget) {
                              bool success =
                                  await dataTargets.removeMembers([dataitem]);
                              if (success) {
                                ToastUtils.showMsg(msg: "移除成员成功");
                                setState(() {});
                              } else {
                                ToastUtils.showMsg(msg: "移除失败，请稍后重试");
                              }
                              Get.back();
                              Get.back();
                            }
                          }, userName: dataitem.name ?? "成员")),
                    ),
                    barrierColor: Colors.black.withOpacity(0.6));
              })
        ],
        isNeedPop: false);
  }

  _buildList(BuildContext context, List<XTarget> datas) {
    return ListWidget(
      initDatas: datas,
      getDatas: ([dynamic parentData]) {
        if (null == parentData) {
          return datas;
        }
        return [];
      },
      // getTitle: (dynamic data) => Text(data.name),
      // getAvatar: (dynamic data) => data is XEntity && null != data.shareIcon()
      //     ? TeamAvatar(size: 35, info: TeamTypeInfo(share: data.shareIcon()))
      //     : XImageWidget.asset(
      //         width: 35,
      //         height: 35,
      //         IconsUtils.workDefaultAvatar(data.typeName)),
      // getLabels: (dynamic data) => data is IEntity ? data.groupTags : null,
      // getDesc: (dynamic data) => "" != data.remark ? Text(data.remark) : null,
      getAction: hasAuth(data)
          ? (dynamic dataitem) {
              return GestureDetector(
                  onTap: () {
                    LogUtil.d('>>>>>>======移除成员');
                    if (hasAuth(data)) {
                      if (isSelf) {
                        _removeFriend(dataitem);
                      } else {
                        _removeMember(dataitem);
                      }
                    } else {
                      ToastUtils.showMsg(msg: "敬请期待！！！");
                    }
                  },
                  child: XImage.localImage(XImage.moreAction));
              // return GestureDetector(
              //   onTap: () {
              //     LogUtil.d('>>>>>>======点击了感叹号');
              //     RoutePages.jumpRelationInfo(data: data);
              //   },
              //   child: const IconWidget(
              //     color: XColors.chatHintColors,
              //     iconData: Icons.info_outlined,
              //   ),
              // );
            }
          : null,
      onTap: (dynamic data, List children) {
        LogUtil.d('>>>>>>======点击了列表项 ${data.name}');
        if (children.isNotEmpty) {
          RoutePages.jumpRelation(parentData: data, listDatas: children);
        } else if (data is XTarget) {
          ISession? session = relationCtrl.user?.findMemberChat(data.id);
          if (null != session) {
            RoutePages.jumpRelationInfo(data: session);
          } else {
            // 待完善新建的会话
            RoutePages.jumpRelationInfo(data: data);
          }
        } else {
          RoutePages.jumpRelationInfo(data: data);
        }
      },
    );
  }
}
