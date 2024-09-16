import 'package:flutter/widgets.dart';
import 'package:orginone/components/common/icons/ximage.dart';

import 'empty_widget.dart';

class EmptyChat extends StatelessWidget {
  const EmptyChat({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return EmptyWidget(title: "暂无聊天记录", iconPath: XImage.emptyChat);
  }
}
