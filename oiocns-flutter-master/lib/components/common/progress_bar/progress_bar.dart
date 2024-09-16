import 'package:flutter/material.dart';
import 'package:orginone/config/theme/unified_style.dart';

///进度条
class OrginoneProgressBar extends StatefulWidget {
  double? progress;
  OrginoneProgressBar({super.key, this.progress});

  @override
  State<StatefulWidget> createState() => _OrginoneProgressBarState();
}

class _OrginoneProgressBarState extends State<OrginoneProgressBar>
    with SingleTickerProviderStateMixin {
  // final double _progress = 0.0;
  late AnimationController _controller;
  late Animation<double> _animation;
  @override
  void initState() {
    super.initState();
    if (null == widget.progress) {
      // 模拟进度更新
      _controller = AnimationController(
        vsync: this,
        duration: const Duration(seconds: 5),
      );
      _animation = Tween<double>(begin: 0.0, end: 0.9).animate(_controller);
      _controller.addListener(() {
        if (mounted) {
          if (_animation.isCompleted) {
            _controller.forward();
          } else {
            setState(() {});
          }
        }
      });
      _controller.forward();
    }
    // Timer.periodic(const Duration(milliseconds: 100), (Timer t) {
    //   setState(() {
    //     if (_progress >= 1) {
    //       t.cancel();
    //     } else {
    //       _progress += 0.01;
    //     }
    //   });
    // });
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  void didUpdateWidget(OrginoneProgressBar oldWidget) {
    super.didUpdateWidget(oldWidget);
  }

  @override
  Widget build(BuildContext context) {
    return LinearProgressIndicator(
      backgroundColor: XColors.statisticsBoxColor,
      valueColor: const AlwaysStoppedAnimation<Color>(XColors.blueTextColor),
      value: widget.progress ?? _animation.value,
    );
  }
}
