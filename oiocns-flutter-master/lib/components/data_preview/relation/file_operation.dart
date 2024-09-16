import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:lpinyin/lpinyin.dart';
import 'package:orginone/components/common/dialogs/bottom_sheet_dialog.dart';
import 'package:orginone/components/common/dialogs/common_widget.dart';
import 'package:orginone/components/common/icons/ximage.dart';
import 'package:orginone/components/common/size/ui_config.dart';
import 'package:orginone/components/common/tab_pages/types.dart';
import 'package:orginone/components/common/tip/toast.dart';
import 'package:orginone/components/data_preview/entity_info/entity_info_page_modal.dart';
import 'package:orginone/components/data_preview/relation/file_detail_modal.dart';
import 'package:orginone/config/theme/unified_style.dart';
import 'package:orginone/dart/base/common/commands.dart';
import 'package:orginone/dart/base/model.dart';
import 'package:orginone/dart/core/chat/session.dart';
import 'package:orginone/dart/core/target/base/team.dart';
import 'package:orginone/dart/core/thing/directory.dart';
import 'package:orginone/dart/core/thing/systemfile.dart';
import 'package:orginone/other/common_fun.dart';
import 'package:orginone/routers/router_const.dart';

bool hasAuth(dynamic data) {
  bool res = false;
  if (data is ISession) {
    data = data.target;
  }
  if (data is ITeam && data.hasRelationAuth() && data.id != data.userId) {
    res = true;
  }
  return res;
}

class FileTaskList extends StatefulWidget {
  IDirectory iDirectory;
  FileTaskList({Key? key, required this.iDirectory}) : super(key: key);

  @override
  State<FileTaskList> createState() => _FileTaskListState();
}

class _FileTaskListState extends State<FileTaskList> {
  late RxList<TaskModel> listTask = <TaskModel>[].obs;
  late IDirectory iDirectory;
  String taskKey = "taskEmitterKey";

  @override
  void initState() {
    iDirectory = widget.iDirectory;
    listTask.value = iDirectory.taskList;
    taskKey = iDirectory.subscribe((key, p1) {
      listTask.value = iDirectory.taskList;
    }, false);
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Material(
          color: Colors.transparent,
          child: Container(
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(10),
              ),
              padding: const EdgeInsets.all(16),
              width: UIConfig.screenWidth - 32,
              height: UIConfig.screenHeight * 0.6,
              child: Column(
                children: [
                  Text(
                    "操作记录",
                    style:
                        TextStyle(fontSize: 24.sp, fontWeight: FontWeight.w600),
                  ),
                  const SizedBox(height: 8),
                  listTask.isEmpty
                      ? const Expanded(child: Center(child: Text('暂无数据')))
                      : Obx(() => Column(
                          children: listTask.value.reversed
                              .map((item) => Container(
                                    padding:
                                        EdgeInsets.symmetric(vertical: 16.h),
                                    width: UIConfig.screenWidth,
                                    decoration: const BoxDecoration(
                                        border: Border(
                                            bottom: BorderSide(
                                                width: 0.5,
                                                color:
                                                    XColors.dividerLineColor))),
                                    child: Row(
                                      children: [
                                        XImage.localImage(XImage.file,
                                            width: 30.w),
                                        const SizedBox(width: 4),
                                        Expanded(
                                            child: Text(
                                          item.name,
                                          overflow: TextOverflow.ellipsis,
                                          maxLines: 1,
                                          style: const TextStyle(fontSize: 16),
                                        )),
                                        item.finished >= 0
                                            ? Text(doubleToPercentage(
                                                (item.finished / 100)
                                                    .toDouble()))
                                            : GestureDetector(
                                                onTap: () {
                                                  // iDirectory.createFile(item.name);
                                                },
                                                child: const Text(
                                                  "上传失败",
                                                  style: TextStyle(
                                                      color: Colors.red),
                                                ),
                                              ),
                                      ],
                                    ),
                                  ))
                              .toList()))
                ],
              ))),
    );
  }
}

fileOperationModal(BuildContext context, {dynamic fileItem, dynamic data}) {
  return buildBottomModal(
      context,
      [
        if (hasAuth(data))
          ActionModel(
              title: fileItem is IDirectory ? "标记删除" : "彻底删除",
              onTap: () async {
                Get.dialog(
                    Center(
                      child: Material(
                          color: Colors.transparent,
                          child: buildConfirmDialogCommon(context, () async {
                            //删除文件
                            bool result = false;
                            if (fileItem is IDirectory) {
                              IDirectory iDirectory = fileItem;
                              result = await iDirectory.delete(notity: true);
                            } else if (fileItem is SysFileInfo) {
                              result = await fileItem.hardDelete(notity: true);
                            }
                            if (!result) {
                              ToastUtils.showMsg(msg: "删除失败，请稍后再试");
                            }
                            command.emitterFlag("refreshFilePage");
                            Get.back();
                            Get.back();
                          })),
                    ),
                    barrierColor: Colors.black.withOpacity(0.6));
              }),
        if (fileItem is SysFileInfo)
          ActionModel(
              title: "下载文件",
              onTap: () async {
                Get.toNamed(Routers.messageFile,
                    arguments: {"file": fileItem.shareInfo()});
              }),
        ActionModel(
            title: "详细信息",
            onTap: () async {
              Get.dialog(
                  Center(
                    child: Material(
                        color: Colors.transparent,
                        child: FileinfoModal(
                          data: fileItem,
                        )),
                  ),
                  barrierColor: Colors.black.withOpacity(0.6));
            })
      ],
      isNeedPop: false);
}



typedef CreateDirectoryCallBack = Function(
    String name, String code, String remark);

Future<void> showCreateDirectoryDialog(BuildContext context,
    {CreateDirectoryCallBack? onCreate,
    String name = '',
    String code = '',
    String remark = '',
    bool isEdit = false}) async {
  TextEditingController nameCrl = TextEditingController(text: name);
  TextEditingController codeCrl = TextEditingController(text: code);
  TextEditingController remarkCrl = TextEditingController(text: remark);
  nameCrl.addListener(() {
    String pinyin = PinyinHelper.getPinyin(nameCrl.text);
    if (pinyin.isEmpty) return;
    String tag = pinyin.split(" ").map((e) => e[0].toUpperCase()).join();
    codeCrl.text = tag;
  });

  return Get.dialog(
      Center(
        child: Material(
          child: Container(
              width: UIConfig.screenWidth - 32,
              padding: const EdgeInsets.all(16),
              child: SingleChildScrollView(
                child: StatefulBuilder(builder: (context, state) {
                  return SizedBox(
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        CommonWidget.commonHeadInfoWidget(
                            isEdit ? "编辑" : "新建目录"),
                        CommonWidget.commonTextTile("名称", '',
                            controller: nameCrl,
                            showLine: true,
                            required: true,
                            maxLine: 1,
                            hint: "请输入",
                            textStyle: XFonts.size22Black0),
                        CommonWidget.commonTextTile("代码", '',
                            controller: codeCrl,
                            showLine: true,
                            required: true,
                            hint: "请输入",
                            maxLine: 1,
                            textStyle: XFonts.size22Black0),
                        CommonWidget.commonTextTile("备注信息", '',
                            controller: remarkCrl,
                            showLine: true,
                            required: true,
                            hint: "请输入",
                            maxLine: 5,
                            textStyle: XFonts.size22Black0),
                        CommonWidget.commonMultipleSubmitWidget(onTap1: () {
                          Navigator.pop(context);
                        }, onTap2: () {
                          if (nameCrl.text.isEmpty) {
                            ToastUtils.showMsg(msg: "请输入名称");
                          } else if (codeCrl.text.isEmpty) {
                            ToastUtils.showMsg(msg: "请输入代码");
                          } else if (remarkCrl.text.isEmpty) {
                            ToastUtils.showMsg(msg: "请输入备注信息");
                          } else {
                            if (onCreate != null) {
                              onCreate(
                                  nameCrl.text, codeCrl.text, remarkCrl.text);
                            }
                            Navigator.pop(context);
                          }
                        }),
                      ],
                    ),
                  );
                }),
              )),
        ),
      ),
      barrierColor: Colors.transparent.withOpacity(0.6));
}
