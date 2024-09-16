import 'dart:io';

import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:orginone/dart/base/model.dart';
import 'package:orginone/dart/core/thing/systemfile.dart';
import 'package:orginone/main_base.dart';

import 'resource_container.dart';
import 'uploading_progress.dart';

/// 文件上传
class FileUpload extends StatefulWidget {
  final void Function(ISysFileInfo sysFile)? onSuccess;
  final void Function(ISysFileInfo sysFile)? onRemove;
  final IconData? uploadButtonIcon;
  final bool allowMultiple;

  const FileUpload(
      {super.key,
      this.onSuccess,
      this.onRemove,
      this.uploadButtonIcon,
      this.allowMultiple = false});

  @override
  State<StatefulWidget> createState() => _FileUploadState();
}

class _FileUploadState extends State<FileUpload> {
  Function(ISysFileInfo sysFile)? get onSuccess => widget.onSuccess;
  Function(ISysFileInfo sysFile)? get onRemove => widget.onRemove;
  bool get allowMultiple => widget.allowMultiple;
  late List<Widget> uploadingWidgetList;

  @override
  void initState() {
    super.initState();
    uploadingWidgetList = [];
  }

  @override
  Widget build(BuildContext context) {
    return ResourceContainerWidget(const [], 100,
        uploadingWidget: uploadingWidgetList,
        uploadButtonWidget: GestureDetector(
          onTap: () async {
            FilePickerResult? result = await FilePicker.platform
                .pickFiles(type: FileType.media, allowMultiple: allowMultiple);
            if (result != null) {
              for (var file in result.files) {
                uploadingWidgetList.add(FileUploading(
                    localFile: file,
                    uploadSuccess: onSuccess,
                    onRemove: (e, sysFileInfo) {
                      uploadingWidgetList.remove(e);
                      if (null != sysFileInfo) {
                        onRemove?.call(sysFileInfo);
                      }
                      setState(() {});
                    }));
              }
              setState(() {});
            }
          },
          child: Container(
            // width: 80.w,
            // height: 80.w,
            margin: const EdgeInsets.only(right: 10, top: 10),
            decoration: BoxDecoration(
              color: Colors.grey.shade200,
              borderRadius: BorderRadius.all(Radius.circular(5.w)),
            ),
            // margin: EdgeInsets.only(bottom: 10.h),
            child: Icon(widget.uploadButtonIcon ?? Icons.upload),
          ),
        ));
  }
}

///文件上传项
class FileUploading extends StatefulWidget {
  final PlatformFile localFile;
  final void Function(ISysFileInfo sysFile)? uploadSuccess;
  final void Function(FileUploading widget, ISysFileInfo? sysFile)? onRemove;
  const FileUploading(
      {super.key, required this.localFile, this.onRemove, this.uploadSuccess});

  @override
  State<StatefulWidget> createState() => _FileUploadingState();
}

class _FileUploadingState extends State<FileUploading> with ResourceMixin {
  PlatformFile get localFile => widget.localFile;
  void Function(ISysFileInfo sysFile)? get uploadSuccess =>
      widget.uploadSuccess;
  void Function(FileUploading widget, ISysFileInfo? sysFile)? get onRemove =>
      widget.onRemove;

  late double _progress;
  late List<Widget> imageProviderList;
  late int index;
  late FileItemShare? fileInfo;
  late ISysFileInfo? sysFile;

  @override
  void initState() {
    super.initState();
    imageProviderList = [];
    index = 0;
    _progress = 0;
    fileInfo = null;
    sysFile = null;
    _filePicked(localFile);
  }

  @override
  Widget build(BuildContext context) {
    return null != fileInfo
        ? UploadingProgress(
            onReupload: () {
              _progress = 0;
              _filePicked(localFile);
            },
            targetWidget:
                renderResource(context, fileInfo!, imageProviderList) ??
                    Container(),
            progress: _progress,
            onRemove: () {
              onRemove?.call(widget, _progress == 0 ? sysFile : null);
            },
          )
        : Container();
  }

  Future<void> _filePicked(PlatformFile selectFile) async {
    var docDir = relationCtrl.user?.directory;
    if (null == selectFile.path || null == docDir) return Future(() => null);
    String ext = selectFile.name.split('.').last;

    var localFile = File(selectFile.path!);
    // selectFile.name;
    // selectFile.path;
    // localFile.lengthSync();
    fileInfo = fileInfo ??
        FileItemShare(
          shareLink: '${selectFile.path}',
          extension: ext,
          size: localFile.lengthSync(),
          name: selectFile.name,
        );
    // fileList.add(fileInfo);2
    // int index = fileList.length;

    sysFile = await docDir.createFile(
      localFile,
      p: (progress) {
        setState(() {
          _progress = progress;
        });
      },
    );
    if (sysFile != null) {
      fileInfo = sysFile?.shareInfo();
      uploadSuccess?.call(sysFile!);
      // setState(() {
      // fileList.insert(index, sysFile.shareInfo());
      // });
    }
  }
}
