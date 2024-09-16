import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:orginone/components/common/extension/index.dart';
import 'package:orginone/components/common/getx/base_get_page_view.dart';
import 'package:orginone/components/common/text/target_text.dart';
import 'package:orginone/config/theme/unified_style.dart';
import 'package:orginone/dart/base/schema.dart';
import 'package:timeline_tile/timeline_tile.dart';

import 'logic.dart';
import 'state.dart';

Map<int, Status> statusMap = {
  1: Status(Colors.blue, '待处理'),
  100: Status(Colors.green, '已同意'),
  200: Status(Colors.red, '已拒绝'),
  102: Status(Colors.green, '已发货'),
  220: Status(Colors.red, '买方取消订单'),
  221: Status(Colors.red, '买方取消订单'),
  222: Status(Colors.red, '已退货'),
};

class Status {
  late Color color;
  late String text;

  Status(this.color, this.text);
}

///历史痕迹页面
class UseTracesPage
    extends BaseGetPageView<UseTracesController, UseTracesState> {
  UseTracesPage({super.key});

  @override
  Widget buildView() {
    return _timeLine();
  }

  Widget _timeLine() {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 10.w),
      height: double.infinity,
      color: XColors.backgroundColor,
      child: Builder(builder: (context) {
        int length = state.flowInstance?.historyTasks?.length ?? 0;
        return ListView.builder(
          itemCount: length,
          itemBuilder: (context, index) {
            return Container(
              child: _buildTimelineTile(
                index,
                state.flowInstance!.historyTasks![index],
              ),
            );
          },
        );
      }),
    );
  }

  Widget _buildTimelineTile(int index, XWorkTaskHistory task) {
    bool isLast =
        index == state.flowInstance!.historyTasks!.length - 1 ? true : false;

    return TimelineTile(
      isFirst: index == 0 ? true : false,
      isLast: isLast,
      indicatorStyle: IndicatorStyle(
        width: 15.w,
        height: 15.w,
        color: XColors.themeColor,
        indicatorXY: 0,
      ),
      afterLineStyle: const LineStyle(thickness: 1, color: XColors.themeColor),
      beforeLineStyle: const LineStyle(thickness: 1, color: XColors.themeColor),
      endChild: Container(
        margin: EdgeInsets.only(left: 15.h),
        child: Card(
          child: Padding(
            padding: EdgeInsets.symmetric(horizontal: 15.w, vertical: 10.h),
            child: Column(
              children: [
                Row(
                  children: [
                    Text(statusMap[task.status]!.text),
                    SizedBox(
                      width: 20.w,
                    ),
                    Expanded(
                        child: TargetText(
                      userId: task.createUser ?? "",
                    )),
                    Text("审核节点:${task.node?.nodeType ?? ""}"),
                  ],
                ),
                SizedBox(
                  height: 30.h,
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Expanded(
                        child: Text(
                      "审批意见:${task.records?.last.comment ?? ""}",
                      overflow: TextOverflow.ellipsis,
                      maxLines: 1,
                    )),
                    Text(DateTime.tryParse(task.createTime ?? "")
                            ?.format(format: "yyyy-MM-dd HH:mm:ss") ??
                        ""),
                  ],
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  @override
  UseTracesController getController() {
    return UseTracesController();
  }

  @override
  String tag() {
    // TODO: implement tag
    return "UseTraces";
  }
}
