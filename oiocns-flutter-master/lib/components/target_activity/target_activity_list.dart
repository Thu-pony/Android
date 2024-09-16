import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:orginone/components/common/templates/orginone_page_widget.dart';
import 'package:orginone/components/target_activity/activity_comment_box.dart';
import 'package:orginone/components/target_activity/target_activity_view.dart';
import 'package:orginone/dart/core/chat/activity.dart';
import 'package:scrollable_positioned_list/scrollable_positioned_list.dart';

import 'activity_message.dart';

//特定Target动态列表
class TargetActivityList extends OrginoneStatelessWidget {
  TargetActivityList({super.key, super.data}) {
    TargetActivityViewController targetActivityViewController =
        TargetActivityViewController();
    if (null != data) {
      Get.lazyPut(() => targetActivityViewController);
      Get.lazyPut(() => ActivityCommentBoxController());
    }
  }
  TargetActivityViewController get controller => Get.find();
  Rxn<IActivity> get activity => controller.state.activity;
  Rxn<IActivityMessage> get activityMessage => controller.state.activityMessage;
  var pageCurrentIndex = 0;

  @override
  Widget buildWidget(BuildContext context, dynamic data) {
    int initIndex = activity.value?.currIndex ?? 0;
    PageController pageController = PageController(initialPage: initIndex);
    pageCurrentIndex = initIndex;
    return Container(
        color: Colors.white,
        child: Obx(
          () => PageView.builder(
              itemBuilder: (context, index) {
                // activityMessage.value = activity.value?.activityList[--index];
                return _buildItem(pageController, index);
              },
              onPageChanged: (index) async {
                pageCurrentIndex = index;
                if (index >= (activity.value?.activityList.length ?? 1) - 3) {
                  await activity.value!.load();
                  activity.value!.changeCallback();
                }
              },
              itemCount: activity.value?.activityList.length ?? 1,
              scrollDirection: Axis.vertical,
              controller: pageController),
        ));
  }

  Widget _buildItem(PageController pageController, int indexItem) {
    double startOffset = 0;
    Widget detail = ActivityCommentBox(
      body: NotificationListener<ScrollNotification>(
          onNotification: (notification) {
            if (notification is ScrollStartNotification) {
              // 开始滚动
              print(">>>>>>>开始滚动 ${notification.metrics.pixels}");
              startOffset = notification.metrics.pixels;
            } else if (notification is ScrollUpdateNotification) {
              // 正在滚动。。。总滚动距离：${notification.metrics.maxScrollExtent}"
              // print("正在滚动。。。总滚动距离：${notification.metrics.maxScrollExtent}");
            } else if (notification is ScrollEndNotification) {
              // activity.value.load();
              // "停止滚动"
              if (notification.metrics.pixels >=
                      notification.metrics.maxScrollExtent &&
                  notification.metrics.atEdge &&
                  pageCurrentIndex <
                      (activity.value!.activityList.length - 1) &&
                  notification.metrics.pixels - startOffset <= 200) {
                pageController.nextPage(
                    duration: const Duration(milliseconds: 500),
                    curve: Curves.linear);
              } else if (notification.metrics.pixels <= 0 &&
                  notification.metrics.atEdge &&
                  pageCurrentIndex > 0 &&
                  notification.metrics.pixels - startOffset <= 200) {
                pageController.previousPage(
                    duration: const Duration(milliseconds: 500),
                    curve: Curves.easeInOut);
              }
              print(">>>>>>>停止滚动 ");
              // print(
              //     ">>>>>${notification.metrics.atEdge} ${notification.metrics.pixels} ${notification.metrics.maxScrollExtent}  ${notification.metrics.minScrollExtent}");
            } else if (notification is OverscrollNotification) {
              if (notification.overscroll > 0) {
                print(">>>>>>end");
              } else if (notification.overscroll < 0) {
                print(">>>>>>start");
              }
            }
            // print("$notification");
            return true;
          },
          child:
              //  RefreshIndicator(
              //     onRefresh: () => controller.state.refresh(),
              //     child:
              ScrollablePositionedList.builder(
            // shrinkWrap: true,
            // key: controller.state.scrollKey,
            // reverse: true,
            physics: const ClampingScrollPhysics(),
            // itemScrollController: controller.state.itemScrollController,
            // addAutomaticKeepAlives: true,
            // addRepaintBoundaries: true,
            itemCount: 1,
            itemBuilder: (BuildContext context, int index) {
              return Container(
                  padding: const EdgeInsets.only(bottom: 10.0),
                  child: ActivityMessageWidget(
                    item: activity.value!.activityList[indexItem],
                    activity: activity.value!.activityList[indexItem].activity,
                  ));
            },
          )),
    );
    return detail;
  }

  // Widget _item(int index) {
  //   if (activityMessage.value != null) {
  //     return ActivityMessageWidget(
  //       item: activity.value!.activityList[pageCurrentIndex],
  //       activity: activity.value!.activityList[pageCurrentIndex].activity,
  //     );
  //   } else if (null != activity.value) {
  //     return ActivityMessageWidget(
  //       item: activity.value!.activityList[index],
  //       activity: activity.value!.activityList[index].activity,
  //     );
  //   }

  //   return Container();
  // }
}
