import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:orginone/config/theme/unified_style.dart';

///上传进度提示
class UploadingProgress extends StatefulWidget {
  final Widget targetWidget;
  double progress;
  void Function()? onReupload;
  void Function()? onRemove;

  UploadingProgress(
      {super.key,
      required this.targetWidget,
      required this.progress,
      this.onReupload,
      this.onRemove});

  @override
  State<StatefulWidget> createState() => _UploadingProgressState();
}

class _UploadingProgressState extends State<UploadingProgress> {
  Widget get targetWidget => widget.targetWidget;
  double get progress => widget.progress;
  void Function()? get onReupload => widget.onReupload;
  void Function()? get onRemove => widget.onRemove;

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Stack(
      alignment: Alignment.center,
      fit: StackFit.passthrough,
      children: [
        Container(
          color: Colors.black.withOpacity(0.1),
          alignment: Alignment.center,
          margin: const EdgeInsets.only(right: 10, top: 10),
          child: targetWidget,
        ),
        if (progress != 1)
          Container(
              margin: const EdgeInsets.only(right: 10, top: 10),
              color: Colors.black.withOpacity(0.6),
              alignment: Alignment.center,
              child: progress >= 0
                  ? CircularProgressIndicator(
                      value: progress,
                      backgroundColor: XColors.statisticsBoxColor,
                      valueColor: const AlwaysStoppedAnimation<Color>(
                          XColors.blueTextColor),
                    )
                  : Text.rich(
                      TextSpan(text: '上传失败', children: [
                        WidgetSpan(
                            child: GestureDetector(
                          onTap: () {
                            onReupload?.call();
                          },
                          child: Text(
                            "重试",
                            style:
                                TextStyle(color: Colors.blue, fontSize: 24.sp),
                          ),
                        ))
                        // TextSpan(
                        //     text: "重试",
                        //     style: TextStyle(color: Colors.blue)),
                      ]),
                      style: TextStyle(color: Colors.white, fontSize: 24.sp))
              // Text(
              //   progress >= 0
              //       ? '${(progress * 100).toStringAsFixed(0)}%'
              //       : '上传失败',
              //   style: TextStyle(color: Colors.white, fontSize: 24.sp),
              // ),
              ),
        // Stack(children: [
        Positioned(
          top: 0,
          right: 0,
          child: GestureDetector(
            onTap: onRemove,
            child:
                const Icon(Icons.highlight_remove_outlined, color: Colors.red),
          ),
        ),
        // ]),
      ],
    );
  }
}
