import 'package:flutter/widgets.dart';
import 'package:orginone/components/common/icons/ximage.dart';

import 'empty_widget.dart';

class EmptyActivity extends StatelessWidget {
  const EmptyActivity({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return EmptyWidget(title: "暂无动态", iconPath: XImage.emptyActivity);
  }
}
