import 'package:extended_nested_scroll_view/extended_nested_scroll_view.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:orginone/components/common/icons/ximage.dart';
import 'package:orginone/components/target_activity/activity_list.dart';
import 'package:orginone/dart/core/chat/activity.dart';

//动态页面
class CohortActivityPage extends StatefulWidget {
  // extends BaseGetListPageView<CohortActivityController, CohortActivityState> {
  late String type;
  late String label;
  late GroupActivity Function()? loadCohortActivity;
  late GroupActivity cohortActivity;
  late ScrollController scrollController;

  CohortActivityPage(
    this.type,
    this.label,
    this.cohortActivity, {
    super.key,
    this.loadCohortActivity,
  }) {
    scrollController = ScrollController();
  }

  @override
  State<StatefulWidget> createState() => _CohortActivityPage();
}

class _CohortActivityPage extends State<CohortActivityPage> {
  GroupActivity get cohortActivity => widget.cohortActivity;
  set cohortActivity(GroupActivity cohortActivity) {
    widget.cohortActivity = cohortActivity;
  }

  late IActivity currentActivity;
  GroupActivity Function()? get loadCohortActivity => widget.loadCohortActivity;
  List<IActivityMessage> activityMessageList = <IActivityMessage>[];
  ScrollController scrollController = ScrollController();

  @override
  void initState() {
    super.initState();
    currentActivity = cohortActivity.activitys.first;
  }

  @override
  Widget build(BuildContext context) {
    if (cohortActivity.activitys.isEmpty && null != loadCohortActivity) {
      cohortActivity = loadCohortActivity!.call();
      currentActivity = cohortActivity.activitys.first;
    }
    return ExtendedNestedScrollView(
      headerSliverBuilder: (BuildContext context, bool innerBoxIsScrolled) {
        if (currentActivity is! GroupActivity || activityMessageList.isEmpty) {
          activityMessageList =
              currentActivity.activitys.first.activityList ?? [];
        }
        return [
          SliverToBoxAdapter(
            child: Container(
              // margin: const EdgeInsets.only(bottom: 12),
              color: Colors.white,
              child: SingleChildScrollView(
                scrollDirection: Axis.horizontal,
                padding: EdgeInsets.only(
                    top: 10.h, left: 10.w, right: 10.w, bottom: 12.w),
                child: Row(
                  children: activityGroup(cohortActivity),
                ),
              ),
            ),
          ),
        ];
      },
      onlyOneScrollInBody: true,
      pinnedHeaderSliverHeightBuilder: () {
        return 0;
      },
      body: cohortActivity.activitys.isNotEmpty
          ? ActivityListWidget(
              scrollController: scrollController,
              activity: currentActivity,
            )
          : Container(),
    );
  }

  // //渲染动态列表
  // List<Widget> GroupActivityItem(
  //     {required GroupActivity cohortActivity, IActivity? activity}) {
  //   return state.activityMessageList.map((item) {
  //     return ActivityMessageWidget(
  //       item: item,
  //       activity: item.activity,
  //       hideResource: true,
  //     );
  //   }).toList();
  // }

  List<Widget> activityGroup(GroupActivity activity) {
    return activity.activitys
        .where((item) => item.activityList.isNotEmpty)
        .map((item) {
      return Padding(
        padding: EdgeInsets.only(left: 8.w, right: 8.w),
        child: GestureDetector(
            onTap: () {
              currentActivity = item;
              activityMessageList = item.activityList;
              scrollController.jumpTo(0.0);
              setState(() {});
            },
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,
              mainAxisSize: MainAxisSize.min,
              children: [
                XImage.entityIcon(item.metadata,
                    size: Size(45.w, 45.w), radius: 4),
                // TeamAvatar(
                //   info: TeamTypeInfo(
                //       share: item.metadata.shareIcon() ??
                //           model.ShareIcon(
                //               name: '', typeName: item.typeName ?? "")),
                //   size: 45.w,
                // ),
                Container(
                    width: 100.w,
                    margin: const EdgeInsets.only(top: 8),
                    child: Text(item.metadata.name!,
                        textAlign: TextAlign.center,
                        overflow: TextOverflow.ellipsis,
                        style: TextStyle(
                          color: currentActivity == item
                              ? const Color(0xff366ef4)
                              : const Color(0xff424a57),
                          fontSize: 11,
                          fontWeight: currentActivity == item
                              ? FontWeight.w600
                              : FontWeight.w400,
                        )))
              ],
            )),
      );
    }).toList();
  }

  // @override
  // CohortActivityController getController() {
  //   return CohortActivityController(type, cohortActivity);
  // }

  // @override
  // String tag() {
  //   return "portal_$type";
  // }

  // @override
  // bool displayNoDataWidget() => false;
}
