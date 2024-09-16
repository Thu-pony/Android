import 'dart:io';
import 'dart:math';

import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:orginone/components/common/button/bottom_button_common.dart';
import 'package:orginone/components/common/icons/icon.dart';
import 'package:orginone/components/common/icons/ximage.dart';
import 'package:orginone/components/common/list/index.dart';
import 'package:orginone/components/common/progress_bar/progress_bar.dart';
import 'package:orginone/components/common/tab_pages/types.dart';
import 'package:orginone/components/common/templates/orginone_page_widget.dart';
import 'package:orginone/components/common/tip/toast.dart';
import 'package:orginone/components/data_preview/relation/file_operation.dart';
import 'package:orginone/components/empty/empty_file.dart';
import 'package:orginone/config/constant.dart';
import 'package:orginone/config/theme/unified_style.dart';
import 'package:orginone/dart/base/common/commands.dart';
import 'package:orginone/dart/base/model.dart';
import 'package:orginone/dart/base/schema.dart';
import 'package:orginone/dart/core/chat/session.dart';
import 'package:orginone/dart/core/public/entity.dart';
import 'package:orginone/dart/core/target/innerTeam/department.dart';
import 'package:orginone/dart/core/target/outTeam/cohort.dart';
import 'package:orginone/dart/core/target/outTeam/group.dart';
import 'package:orginone/dart/core/target/person.dart';
import 'package:orginone/dart/core/target/team/company.dart';
import 'package:orginone/dart/core/thing/directory.dart';
import 'package:orginone/dart/core/thing/standard/property.dart';
import 'package:orginone/dart/core/thing/systemfile.dart';
import 'package:orginone/main_base.dart';
import 'package:orginone/routers/pages.dart';
import 'package:orginone/utils/file_utils.dart';
import 'package:orginone/utils/log/log_util.dart';

/// 文件列表页面
class FileListPage extends OrginoneStatefulWidget {
  FileListPage({super.key, super.data});
  @override
  State<StatefulWidget> createState() => _FileListPageState();
}

class _FileListPageState extends OrginoneStatefulState<FileListPage, dynamic> {
  RxDouble progressUploud = 0.0.obs;
  RxList<IEntity> directorys = <IEntity>[].obs;
  RxList<TaskModel> listTask = <TaskModel>[].obs;
  dynamic currentDic;
  bool isCalculate = false;
  String uploudKey = "";
  String emitKey = "emitKey";

  @override
  void initState() {
    super.initState();
    directorys.value = getCurrentDirectory(data);
    emitKey = command.subscribeByFlag("refreshFilePage", ([args]) async {
      if (currentDic is IDirectory) {
        await currentDic.loadContent(reload: true);
      }
      directorys.value = getCurrentDirectory(data);
    });
  }

  @override
  Widget buildWidget(BuildContext context, dynamic data) {
    Widget content = Obx(() => directorys.isNotEmpty
          ? Container(
              margin: EdgeInsets.symmetric(vertical: 1.h),
              decoration: const BoxDecoration(color: Colors.white),
              child: Obx(() => _buildList(context, directorys.value)))
          : const EmptyFile());
    
    return addBottomBtnWidget(
        child: Column(
          children: [Expanded(child: content), buildProgress()],
        ),
        context: context);
  }

  List<IEntity> getCurrentDirectory(dynamic data) {
    List<IEntity> directorys = [];
    if (data is IPerson) {
      currentDic = data.directory;
      directorys = loadAll<IDirectory>(data.directory);
    } else if (data is ICompany) {
      currentDic = data.directory;
      directorys = loadAll<IDirectory>(data.directory);
    } else if (data is IDepartment) {
      currentDic = data.directory;
      directorys = loadAll<IDirectory>(data.directory);
    } else if (data is ICohort) {
      currentDic = data.directory;
      directorys = loadAll<IDirectory>(data.directory);
    } else if (data is IGroup) {
      currentDic = data.directory;
      directorys = loadAll<IDirectory>(data.directory);
    } else if (data is IDirectory) {
      currentDic = data;
      directorys = loadAll<IDirectory>(data);
    } else if (data is ISession) {
      currentDic = data.target.directory;
      directorys = loadAll<IDirectory>(currentDic);
    }
    return directorys;
  }

  buildProgress() {
    return Obx(() => progressUploud > 0
        ? OrginoneProgressBar(progress: progressUploud.value)
        : Container());
  }

  List<IEntity> loadAll<T extends IDirectory>(T targtet) {
    List<IEntity> directorys = [];
    directorys.addAll(loadDirectorys<T>(targtet));
    directorys.addAll(loadFiels<T>(targtet));
    directorys.addAll(loadPropertys<T>(targtet));
    directorys = _filterDeleteed(directorys);

    return directorys;
  }

  ///过滤已删除的目录
  List<IEntity> _filterDeleteed(List<IEntity> directorys) {
    return directorys
        .where((element) => !element.groupTags.contains("已删除"))
        .toList();
  }

  List<IDirectory> loadDirectorys<T extends IDirectory>(T target) {
    List<IDirectory> directorysTemp = target.children;
    LogUtil.d('>>>>>>======directorys ${directorysTemp.length}');

    if (directorysTemp.isEmpty) {
      target.loadContent().then((value) {
        if (value && target.children.isNotEmpty) {
          directorys.value = getCurrentDirectory(data);
        }
      });
    }
    return directorysTemp;
  }

  ///加载文件
  List<ISysFileInfo> loadFiels<T extends IDirectory>(T target) {
    List<ISysFileInfo> files = target.files;
    LogUtil.d('>>>>>>======files ${files.length}');
    if (files.isEmpty) {
      target.loadContent().then((value) {
        if (value && target.files.isNotEmpty) {
          directorys.value = getCurrentDirectory(data);
        }
      });
    }
    return files;
  }

  ///加载属性
  List<IProperty> loadPropertys<T extends IDirectory>(T target) {
    List<IProperty> propertys = target.standard.propertys;
    print('>>>>>>======propertys ${propertys.length}');
    if (propertys.isEmpty) {
      target.standard.loadPropertys().then((value) {
        if (value.isNotEmpty) {
          directorys.value = getCurrentDirectory(data);
        }
      });
    }
    return propertys;
  }

  Widget addBottomBtnWidget(
      {required Widget child, required BuildContext context}) {
    return GyPageWithBottomBtn(
      body: child,
      listAction: [
        ActionModel(
            title: "新建",
            onTap: () async {
              showCreateDirectoryDialog(
                context,
                onCreate: (name, code, remark) async {
                  IDirectory iDirectory = currentDic;
                  XDirectory xx = XDirectory(
                      directoryId: iDirectory.directoryId,
                      id: '',
                      isDeleted: false);
                  xx.name = name;
                  xx.code = code;
                  xx.remark = remark;
                  await iDirectory.create(xx);
                  await currentDic.loadContent(reload: true);
                  directorys.value = getCurrentDirectory(data);
                },
              );
            }),
        ActionModel(
            title: "上传",
            onTap: () async {
              FilePickerResult? result = await FilePicker.platform
                  .pickFiles(type: FileType.any, allowMultiple: true);
              if (result != null) {
                RoutePages.jumpUploadFiles(filePicker: result);
                uploudKey = Random().nextDouble().toString();

                for (var file in result.files) {
                  var item = await currentDic?.createFile(File(file.path!),
                      p: (progress) {
                    // progressUploud.value = progress;
                    if (isCalculate) return;
                    progressUploud.value = getProgressTotal(result.files);
                  }, tempKey: uploudKey);
                  directorys.value = getCurrentDirectory(data);
                }
                if (progressUploud.value >= 1) {
                  progressUploud.value = 0.0;
                }
                directorys.value = getCurrentDirectory(data);
              }
            }),
        ActionModel(
            title: "上传列表",
            onTap: () {
              Future.delayed(const Duration(milliseconds: 500), () {
                showUploudList(context);
              });
            }),
      ],
    );
  }

  double getProgressTotal(List<PlatformFile> files) {
    isCalculate = true;
    listTask.value = currentDic.taskList;
    int percentageSize = 0;
    int totalSize = 0;

    for (var fileItem in files) {
      for (var taskItem in listTask.value) {
        String fileName = fileItem.path!.split("/").last;
        if (taskItem.name == fileName && taskItem.tempKey == uploudKey) {
          percentageSize = percentageSize +
              ((taskItem.finished / 100 * fileItem.size).toInt());
          break;
        }
      }
      totalSize = totalSize + fileItem.size;
    }
    isCalculate = false;
    print("percentageSize/totalSize====>>${percentageSize / totalSize}");
    return percentageSize / totalSize;
  }

  showUploudList(BuildContext context) {
    listTask.value = currentDic.taskList;
    Get.dialog(FileTaskList(iDirectory: currentDic),
        barrierColor: Colors.black.withOpacity(0.6));
  }

  _buildList(BuildContext context, List datas) {
    return ListWidget(
      initDatas: datas,
      getDatas: ([dynamic parentData]) {
        if (null == parentData) {
          return datas;
        }
        LogUtil.d('>>>>>>======${parentData.runtimeType}');
        return [];
      },
      getAction: (dynamic dataitem) {
        return GestureDetector(
            onTap: () {
              fileOperationModal(context, fileItem: dataitem, data: data);
            },
            child: XImage.localImage(XImage.moreAction));
      },
      // (dynamic data) {
      //   return GestureDetector(
      //       onTap: () {
      //         LogUtil.d('>>>>>>======点击了感叹号');
      //         // RoutePages.jumpEneityInfo(data: data);
      //       },
      //       child: const IconWidget(
      //         color: XColors.chatHintColors,
      //         iconData: Icons.info_outlined,
      //       ));
      // },
      onTap: (dynamic item, List children) {
        LogUtil.d(
            '>>>>>>======点击了列表项 ${item.name} ${item.id} ${children.length}');
        if (item is IDirectory) {
          RoutePages.jumpFileList(data: item);
        } else if (item is SysFileInfo) {
          var extension = item.shareInfo().extension?.toLowerCase() ?? "";

          if (FileUtils.isImage(extension)) {
            //图片左右滑动加载
            var initIndex = 0;
            List<String> listRes = [];
            List listFile = datas.whereType<SysFileInfo>().toList();
            for (var element in listFile) {
              var extension =
                  element.shareInfo().extension?.toLowerCase() ?? "";
              if (FileUtils.isImage(extension)) {
                listRes.add(getFilePath(element));
              }
            }
            initIndex = listRes.indexOf(getFilePath(item));
            RoutePages.jumpImagePageview(listRes, initIndex);
          } else {
            RoutePages.jumpFile(file: item.shareInfo());
          }
        } else {
          RoutePages.jumpRelationInfo(data: item);
        }
      },
    );
  }

  getFilePath(dynamic item) {
    return item.shareInfo().shareLink != null &&
            item.shareInfo().shareLink!.contains('http')
        ? item.shareInfo().shareLink!
        : '${Constant.host}${item.shareInfo().shareLink}';
  }
}
