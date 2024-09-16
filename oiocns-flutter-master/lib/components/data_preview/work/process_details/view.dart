import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:orginone/components/common/getx/base_get_view.dart';
import 'package:orginone/components/common/templates/gy_scaffold.dart';
import 'package:orginone/components/common/text/text.dart';
import 'package:orginone/components/data_preview/work/apply_widget.dart';
import 'package:orginone/components/data_preview/work/process_details/use_traces/view.dart';
import 'package:orginone/config/theme/unified_style.dart';
import 'logic.dart';
import 'process_info/view.dart';
import 'state.dart';

///办事 详情
class ProcessDetailsPage
    extends BaseGetView<ProcessDetailsController, ProcessDetailsState> {
  const ProcessDetailsPage({super.key});

  @override
  Widget buildView() {
    return GyScaffold(
      titleName: state.todo?.taskdata.title,
      leadingWidth: 24,
      body: _buildMainView(),
    );
  }

  _buildMainView() {
    if (state.todo?.instance != null) {
      //流程视图
      // return const Text('data');
      return _buildInstanceView();
    }
    if (state.todo!.targets.isNotEmpty) {
      return ApplyWidget(
        todo: state.todo,
      );
    } else {
      return const Center(
        child: TextWidget(text: '数据异常'),
      );
    }
  }

  Widget tabBar() {
    return Container(
      color: Colors.white,
      alignment: Alignment.centerLeft,
      child: TabBar(
        controller: state.tabController,
        tabs: tabTitle.map((e) {
          return Tab(
            text: e,
            height: 60.h,
          );
        }).toList(),
        indicatorSize: TabBarIndicatorSize.label,
        indicatorColor: XColors.themeColor,
        unselectedLabelColor: Colors.grey,
        unselectedLabelStyle: TextStyle(fontSize: 22.sp),
        labelColor: XColors.themeColor,
        labelStyle: TextStyle(fontSize: 24.sp),
        isScrollable: true,
      ),
    );
  }

  _buildInstanceView() {
    return Column(
      children: [
        tabBar(),
        Expanded(
          child: TabBarView(
            controller: state.tabController,
            children: [
              ProcessInfoPage(),
              UseTracesPage(),
            ],
          ),
        )
      ],
    );
  }
}
