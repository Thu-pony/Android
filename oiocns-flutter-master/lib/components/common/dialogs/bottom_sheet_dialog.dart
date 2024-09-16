import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_custom_dialog/flutter_custom_dialog.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:orginone/components/common/tab_pages/types.dart';
import 'package:orginone/config/theme/unified_style.dart';

typedef ClickCallback = void Function(int index, String title);
typedef SelectCallback<T> = void Function(T model);
@Deprecated("待统一优化")
YYDialog YYBottomSheetDialog(BuildContext context, List<String> titles,
    {bool hasCancel = false, ClickCallback? callback}) {
  return YYDialog().build(context)
    ..gravity = Gravity.bottom
    ..gravityAnimationEnable = true
    ..backgroundColor = Colors.white
    ..widgetList = [
      ...titles
          .map((e) => BottomSheetItem(context,
              title: e, index: titles.indexOf(e), callback: callback))
          .toList(),
      hasCancel ? BottomSheetItem(context, isCancel: true) : const SizedBox(),
    ]
    ..show();
}

@Deprecated("待优化")
Widget BottomSheetItem(BuildContext context,
    {String title = '',
    int? index,
    bool isCancel = false,
    ClickCallback? callback}) {
  return Container(
    width: double.infinity,
    height: 55,
    margin: isCancel
        ? const EdgeInsets.only(bottom: 0, left: 0, right: 0, top: 10)
        : const EdgeInsets.only(bottom: 1, left: 0, right: 0, top: 0),
    decoration: const BoxDecoration(
      // borderRadius: BorderRadius.circular(8.0),
      color: Colors.white,
    ),
    child: InkWell(
      onTap: () {
        Navigator.pop(context);
        if (!isCancel && callback != null) {
          callback(index!, title);
        }
      },
      child: Center(
        child: Text(
          isCancel ? "取消" : title,
          style: TextStyle(
            fontSize: 14,
            color: isCancel
                ? Colors.black45
                : Theme.of(context).colorScheme.secondary,
          ),
        ),
      ),
    ),
  );
}

@Deprecated("待优化")
class PickerUtils {
  static void showVideoMethodsPicker(BuildContext context,
      {required List<String> videoMethods,
      required SelectCallback<String> callback}) {
    showCupertinoPicker(
      context,
      titles: videoMethods,
      callback: (index, title) {
        callback(videoMethods[index]);
      },
    );
  }

  static void showDynamicPicker(BuildContext context,
      {required List<dynamic> models,
      required SelectCallback<dynamic> callback}) {
    showCupertinoPicker(
      context,
      titles: models.map((e) => e.name.toString()).toList(),
      callback: (index, title) {
        callback(models[index]);
      },
    );
  }

  static void showMapPicker(
    BuildContext context, {
    required Map<String, String> map,
    required SelectCallback<String> callback,
    int initIndex = 0,
  }) {
    showCupertinoPicker(
      context,
      titles: map.values.toList(),
      initIndex: initIndex,
      callback: (index, title) {
        callback(map.keys.toList()[index]);
      },
    );
  }

  static void showListStringPicker(BuildContext context,
      {required List<String> titles,
      required SelectCallback<String> callback,
      String doneTitle = "确定"}) {
    showCupertinoPicker(context, titles: titles, callback: (index, title) {
      callback(title);
    }, doneTitle: doneTitle);
  }

  static void showTestPicker(BuildContext context) {
    showCupertinoPicker(
      context,
      titles: [
        "男",
        "女",
        "保密",
      ],
      callback: (index, title) {},
    );
  }
}

showCupertinoPicker(BuildContext context,
    {required List<String> titles,
    required ClickCallback callback,
    String cancelTitle = "取消",
    String doneTitle = "同意",
    int initIndex = 0}) {
  int index = 0;
  showCupertinoModalPopup(
      context: context,
      builder: (BuildContext context) {
        return Container(
          height: 250,
          decoration: const BoxDecoration(
            color: Colors.white,
          ),
          child: Column(
            children: [
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  CupertinoButton(
                    onPressed: () {
                      Navigator.pop(context);
                    },
                    child: Text(
                      cancelTitle,
                      style: const TextStyle(
                        fontSize: 15,
                        color: Colors.black38,
                      ),
                    ),
                  ),
                  CupertinoButton(
                    onPressed: () {
                      Navigator.pop(context);
                      callback(index, titles[index]);
                    },
                    child: Text(
                      doneTitle,
                      style: TextStyle(
                        fontSize: 15,
                        color: Theme.of(context).colorScheme.secondary,
                      ),
                    ),
                  ),
                ],
              ),
              Expanded(
                child: CupertinoPicker(
                  itemExtent: 40,
                  scrollController:
                      FixedExtentScrollController(initialItem: initIndex),
                  onSelectedItemChanged: (value) {
                    index = value;
                  },
                  children: titles
                      .map((e) => Container(
                            height: 40,
                            alignment: Alignment.center,
                            child: Text(
                              e,
                              style: const TextStyle(
                                fontSize: 15,
                              ),
                            ),
                          ))
                      .toList(),
                ),
              ),
            ],
          ),
        );
      });
}

buildBottomModal(BuildContext context, List<ActionModel> listAction,
    {bool isNeedPop = true}) {
  return showModalBottomSheet(
    context: context,
    shape: const RoundedRectangleBorder(
      borderRadius: BorderRadius.only(
        topLeft: Radius.circular(15),
        topRight: Radius.circular(15),
      ),
    ),
    builder: (BuildContext context) {
      return Container(
        padding: EdgeInsets.only(bottom: 40.h),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: listAction
              .map((itemObj) => GestureDetector(
                  onTap: () {
                    itemObj.onTap();
                    if (isNeedPop) Navigator.pop(context);
                  },
                  child: Container(
                    height: 80.h,
                    alignment: Alignment.center,
                    decoration: const BoxDecoration(
                      border: Border(
                        bottom: BorderSide(
                          width: 1,
                          color: XColors.dividerLineColor,
                        ),
                      ),
                    ),
                    child: Text(
                      itemObj.title,
                      style: TextStyle(
                        color: Colors.black,
                        fontSize: 24.sp,
                      ),
                    ),
                  )))
              .toList(),
        ),
      );
    },
  );
}
