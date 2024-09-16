import 'package:flutter/material.dart';
import 'package:orginone/components/common/icons/icon.dart';
import 'package:orginone/components/common/tab_pages/infoListPage.dart';
import 'package:orginone/components/common/tab_pages/types.dart';
import 'package:orginone/components/common/list/index.dart';
import 'package:orginone/config/theme/unified_style.dart';
import 'package:orginone/dart/base/schema.dart';
import 'package:orginone/dart/controller/index.dart';
import 'package:orginone/dart/core/public/entity.dart';
import 'package:orginone/dart/core/public/enums.dart';
import 'package:orginone/dart/core/target/base/target.dart';
import 'package:orginone/dart/core/target/outTeam/storage.dart';
import 'package:orginone/dart/core/target/person.dart';
import 'package:orginone/dart/core/target/team/company.dart';
import 'package:orginone/dart/core/thing/directory.dart';
import 'package:orginone/dart/core/thing/fileinfo.dart';
import 'package:orginone/dart/core/thing/standard/index.dart';
import 'package:orginone/dart/core/thing/standard/index_standart.dart';
import 'package:orginone/dart/core/thing/standard/page.dart';
import 'package:orginone/dart/core/thing/systemfile.dart';
import 'package:orginone/dart/core/work/index.dart';
import 'package:orginone/main_base.dart';
import 'package:orginone/routers/pages.dart';
import 'package:orginone/utils/log/log_util.dart';

/// 关系页面
class StorePage extends StatefulWidget {
  // late InfoListPageModel? storeModel;
  // late dynamic datas;

  // StorePage({super.key, dynamic datas}) {
  //   storeModel = null;
  //   this.datas = datas ?? RoutePages.getRouteParams(homeEnum: HomeEnum.store);
  // }
  const StorePage({super.key});
  @override
  State<StatefulWidget> createState() => _StorePageState();
}

class _StorePageState extends State<StorePage> {
  // InfoListPageModel? get storeModel => widget.storeModel;
  // set storeModel(InfoListPageModel? storeModel) {
  //   widget.storeModel = storeModel;
  // }

  // dynamic get datas => widget.datas;
  // set datas(dynamic value) {
  //   widget.datas = value;
  // }

  late InfoListPageModel? storeModel;
  late dynamic datas;
  _StorePageState() {
    // if (relationCtrl.homeEnum.value == HomeEnum.store &&
    //     RoutePages.getRouteLevel() > 0) {
    // relationCtrl.homeEnum.listen((homeEnum) {
    //   if (homeEnum == HomeEnum.store) {
    //     if (mounted) {
    //       setState(() {
    //         storeModel = null;
    //         datas = RoutePages.getRouteParams(homeEnum: HomeEnum.store);
    //       });
    //     }
    //   }
    // });
    // }
  }
  @override
  void initState() {
    super.initState();
    storeModel = null;
    datas = RoutePages.getRouteParams(homeEnum: HomeEnum.store);
    // command.subscribeByFlag("homeChange", ([List<dynamic>? args]) {
    //   LogUtil.d("did homeChange 3");
    //   if (mounted) {
    //     setState(() {
    //       storeModel = null;
    //       datas = RoutePages.getRouteParams(homeEnum: HomeEnum.store);
    //     });
    //   }
    // });
  }

  @override
  void didUpdateWidget(StorePage oldWidget) {
    super.didUpdateWidget(oldWidget);
    // storeModel = oldWidget.storeModel;
    // datas = oldWidget.datas;
  }

  @override
  Widget build(BuildContext context) {
    if (null == storeModel) {
      // datas = RoutePages.getRouteParams();
      load();
    }

    return InfoListPage(storeModel!);
  }

  void load() {
    storeModel =
        InfoListPageModel(title: RoutePages.getRouteTitle() ?? "数据", tabItems: [
      createTabItemsModel(title: "全部"),
      if (null == datas) ...[
        createTabItemsModel(title: "个人"),
        createTabItemsModel(title: "单位")
      ],
    ]);
    // relationCtrl.user?.loadMembers();
  }

  TabItemsModel createTabItemsModel({
    required String title,
  }) {
    List<IEntity<dynamic>> initDatas = [];
    if (null == datas) {
      initDatas = getFirstLevelDirectories(title);
    } else if (datas is List<IEntity<dynamic>>) {
      initDatas = datas;
    }
    return TabItemsModel(
        title: title,
        content: ListWidget(
          initDatas: initDatas,
          getLazyDatas: ([dynamic parentData]) async {
            // LogUtil.e('目录 parentData');
            // LogUtil.d(parentData.runtimeType);
            // LogUtil.d("typeName:${parentData.typeName}");
            // LogUtil.d("name:${parentData.name} id:${parentData.id}");

            if (null == parentData) {
              return loadStorages(RoutePages.getRootRouteParam());
            } else if (parentData is ICompany &&
                parentData.typeName == TargetType.company.label) {
              return loadStorages(parentData);
            } else if (parentData is IPerson &&
                parentData.typeName == TargetType.person.label) {
              return loadStorages(parentData);
            } else if (parentData is IStorage &&
                parentData.typeName == TargetType.storage.label) {
              return loadStoragesDirectory(parentData);
            } else if (parentData.typeName == SpaceEnum.directory.label &&
                [
                  SpaceEnum.dataStandards.label,
                  SpaceEnum.businessModeling.label,
                  // SpaceEnum.applications.label,
                  SpaceEnum.view.label,
                ].contains(parentData.name) &&
                parentData is Directory) {
              return loadSystemDirectory(parentData);
            } else if (parentData.typeName == SpaceEnum.applications.label &&
                parentData is Application) {
              // XDirectory tmpDir;
              // tmpDir = XDirectory(
              //     id: parentData.id,
              //     directoryId: parentData.id,
              //     isDeleted: false);
              // tmpDir.name = parentData.typeName;
              // tmpDir.typeName = parentData.typeName;
              // Directory directory = Directory(
              //   tmpDir,
              //   relationCtrl.user!,
              // );
              // return loadSystemDirectory(parentData);
              return loadApplicationDirectory(parentData);
            } else if (parentData.typeName == SpaceEnum.directory.label &&
                parentData.name == DirectoryType.property.label &&
                parentData is Directory) {
              return loadProperties(parentData);
            } else if (parentData.typeName == SpaceEnum.directory.label &&
                parentData.name == DirectoryType.species.label &&
                parentData is Directory) {
              return loadSpecies(parentData);
            } else if (parentData.typeName == SpaceEnum.directory.label &&
                parentData.name == DirectoryType.dict.label &&
                parentData is Directory) {
              return loadDicts(parentData);
            } else if (parentData.typeName == SpaceEnum.directory.label &&
                parentData.name == DirectoryType.work.label &&
                parentData is FixedDirectory) {
              return loadWorks(parentData);
            } else if (parentData.typeName == SpaceEnum.directory.label &&
                parentData.name == DirectoryType.file.label &&
                parentData is FixedDirectory) {
              //系统文件类型

              return loadFixedFiles(parentData.parent!.superior as Directory);
            } else if (parentData.typeName == SpaceEnum.directory.label &&
                parentData is Directory &&
                ![
                  DirectoryType.form.label,
                  DirectoryType.report.label,
                  DirectoryType.app.label,
                  SpaceEnum.table.label,
                  SpaceEnum.bulletinBoard.label,
                  SpaceEnum.module.label,
                  SpaceEnum.role.label,
                  SpaceEnum.transfer.label,
                  SpaceEnum.dataMigration.label,
                  SpaceEnum.shoppingPage.label,
                  SpaceEnum.code.label,
                  DirectoryType.mirror.label,
                  DirectoryType.model.label,
                  DirectoryType.form.label,
                  DirectoryType.pageTemplate.label,
                ].contains(parentData.name)) {
              //    dict species property   work app  dataStandard storage

              //系统文件类型--子目录
              return loadFixedFiles(parentData);
            } else if (parentData.typeName == SpaceEnum.directory.label &&
                parentData.name == DirectoryType.app.label &&
                parentData is FixedDirectory) {
              return loadApps(parentData);
            } else if (parentData.typeName == SpaceEnum.directory.label &&
                parentData.name == DirectoryType.model.label &&
                parentData is Directory) {
              // return loadApps(parentData);
            } else if (parentData.typeName == SpaceEnum.directory.label &&
                parentData.name == DirectoryType.form.label &&
                parentData is FixedDirectory) {
              return loadForms(parentData);
            } else if (parentData.typeName == SpaceEnum.directory.label &&
                parentData.name == DirectoryType.pageTemplate.label &&
                parentData is Directory) {
              return loadPageTemplates(parentData);
            }

            return [];
          },
          getAction: (dynamic data) {
            if (data is! IDirectory) {
              return GestureDetector(
                onTap: () {
                  LogUtil.d('>>>>>>======点击了感叹号');
                  RoutePages.jumpStoreInfoPage(data: data);
                },
                child: const IconWidget(
                  color: XColors.chatHintColors,
                  iconData: Icons.info_outlined,
                ),
              );
            }
            return null;
          },
          onTap: (dynamic data, List children) {
            LogUtil.d('>>>>>>======点击了列表项 ${data.name} ${children.length}');
            RoutePages.jumpStore(parentData: data, listDatas: children);
          },
        ));
  }

  // 获得一级目录
  List<IEntity<dynamic>> getFirstLevelDirectories(String title) {
    List<IEntity<dynamic>> datas = [];
    if (null != relationCtrl.user) {
      if (title == "个人" || title == "全部") {
        datas.add(relationCtrl.user!);
      }
      if (title == "单位" || title == "全部") {
        datas.addAll(
            relationCtrl.user?.companys.map((item) => item).toList() ?? []);
      }
    }
    return datas;
  }

  // 加载存储列表
  List<IStorage> loadStorages(ITarget target) {
    TargetType? type = TargetType.getType(target.typeName);
    List<IStorage> storages = [];
    if (type == TargetType.person) {
      storages = relationCtrl.user?.storages ?? [];
    } else if (type == TargetType.company) {
      storages = relationCtrl.user?.findCompany(target.id)?.storages ?? [];
    }

    return storages;
  }

  /// 加载存储列表
  List<Directory> loadStoragesDirectory(IStorage data) {
    List<Directory> datas = [];
    XDirectory tmpDir;
    int id = 0;
    if (null != relationCtrl.user) {
      DirectoryGroupType.getType(data.typeName)?.types.forEach((e) {
        tmpDir = XDirectory(
            id: "${data.id}_$id",
            directoryId: "${data.id}_$id",
            isDeleted: false);
        tmpDir.name = e.label;
        tmpDir.typeName = SpaceEnum.directory.label;
        datas.add(FixedDirectory(
          tmpDir,
          relationCtrl.user!,
          standard: getCurrentCompany()?.standard,
          parent: data.directory,
        ));
        id++;
      });
    }
    return datas;
  }

  /// 加载系统目录
  List<Directory> loadSystemDirectory<T extends StandardFileInfo>(T item) {
    List<Directory> datas = [];
    XDirectory tmpDir;
    int id = 0;
    String type = item is Application ? item.typeName : item.name;
    if (null != relationCtrl.user) {
      DirectoryGroupType.getType(type)?.types.forEach((e) {
        // LogUtil.i("loadSystemDirectory=id:${item.id}");
        tmpDir = XDirectory(
            id: "${item.id}_$id",
            directoryId: "${item.id}_$id",
            isDeleted: false);
        tmpDir.name = e.label;
        tmpDir.typeName = SpaceEnum.directory.label;
        datas.add(FixedDirectory(tmpDir, relationCtrl.user!,
            standard: getCurrentCompany()?.standard));
        id++;
      });
    }
    return datas;
  }

  /// 获得当前单位
  ICompany? getCurrentCompany({String? companyId}) {
    return null != relationCtrl.user
        ? relationCtrl.user!
            .findCompany(companyId ?? RoutePages.getRootRouteParam().id)
        : null;
  }

  /// 加载属性
  Future<List<IProperty>> loadProperties(Directory item) async {
    List<IProperty> files = item.standard.propertys;
    LogUtil.d('>>>>>>======loadProperties ${files.length}');
    if (files.isEmpty) {
      return await item.standard.loadPropertys();
    }
    return files;
  }

  ///加载分类
  Future<List<ISpecies>> loadSpecies(Directory item) async {
    List<ISpecies> files = item.standard.specieses;
    LogUtil.d('>>>>>>======loadSpecies ${files.length}');
    if (files.isEmpty) {
      return await item.standard.loadSpecieses();
    }
    return files;
  }

  ///加载字典
  Future<List<ISpecies>> loadDicts(Directory item) async {
    List<ISpecies> files = item.standard.dicts;
    LogUtil.d('>>>>>>======loadDicts ${files.length}');
    if (files.isEmpty) {
      return await item.standard.loadDicts();
    }
    return files;
  }

  ///加载应用
  Future<List<IApplication>> loadApps(Directory item) async {
    List<IApplication> files = [];
    files = await item.standard.loadStoreApplications();
    //过滤已删除目录
    files = files.where((e) => !e.groupTags.contains("已删除")).toList();
    return files;
  }

  ///加载应用
  Future<List<IWork>> loadWorks(FixedDirectory item) async {
    List<IWork> works = await item.belongApplication!.loadWorks();

    //过滤已删除目录
    // works = works.where((e) => !e.groupTags.contains("已删除")).toList();
    return works;
  }

  Future<List<IEntity>> loadFixedFiles<T extends IDirectory>(T target) async {
    //加载子目录
    List<IDirectory> directorys = target.children;
    if (directorys.isEmpty) {
      await target.loadContent();
      directorys = target.children;
    }
    //过滤已删除目录
    directorys = directorys.where((e) => !e.groupTags.contains("已删除")).toList();
    //加载文件
    List<ISysFileInfo> files = target.files;
    if (files.isEmpty) {
      // await target.loadContent();
      await target.loadFiles();
      files = target.files;
    }
    //过滤非文件类型数据
    List<IEntity> entitys = [];
    entitys.addAll(directorys);
    entitys.addAll(files);
    // directorys = _filterDeleteed(directorys);

    return entitys;
  }

  ///加载文件
  Future<List<ISysFileInfo>> loadFiles<T extends IDirectory>(T target) async {
    List<ISysFileInfo> files = target.files;
    LogUtil.d('>>>>>>======files ${files.length}');
    if (files.isEmpty) {
      // await target.loadContent();
      await target.loadFiles();
      return target.files;
    }
    return files;
  }

  ///@ 加载文件
  Future<List<IStandardFileInfo<XStandard>>> loadFiles2(Directory item) async {
    List<IStandardFileInfo<XStandard>> files = item.standard.standardFiles;
    LogUtil.e('>>>>>>======loadFiles ${files.length}');
    if (files.isEmpty) {
      return await item.standard.loadStandardFiles();
    }

    return files;
  }

  /// 加载应用目录
  List<Directory> loadApplicationDirectory(Application item) {
    LogUtil.i("loadApplicationDirectory applicationId:${item.id}");
    List<Directory> datas = [];
    XDirectory tmpDir;
    int id = 0;
    if (null != relationCtrl.user) {
      DirectoryGroupType.getType(item.typeName)?.types.forEach((e) {
        tmpDir = XDirectory(
            id: "${item.id}_$id",
            directoryId: "${item.id}_$id",
            isDeleted: false);
        tmpDir.name = e.label;
        tmpDir.typeName = SpaceEnum.directory.label;
        // datas.add(Directory(
        //   tmpDir,
        //   relationCtrl.user!
        // ));
        // datas.add(FixedDirectory(tmpDir, relationCtrl.user!,
        //     standard: getCurrentCompany()?.standard, belongApplication: item));

        StandardFiles standardFiles =
            StandardFiles(Directory(item.metadata, item.directory.target));
        // standardFiles.directory.directoryId = item.id;
        datas.add(FixedDirectory(tmpDir, relationCtrl.user!,
            standard: standardFiles, belongApplication: item));
        id++;
      });
    }
    return datas;
  }

  ///加载表单
  Future<List<IForm>> loadForms(FixedDirectory item) async {
    List<IForm> files = item.standard.forms;
    LogUtil.e('>>>>>>======loadForms ${item.id}');
    LogUtil.e('>>>>>>======loadForms ${item.belongApplication?.id}');
    if (files.isEmpty) {
      return await item.standard.loadForms(
          reload: true, applicationId: item.belongApplication?.id); //
    }

    return files;
  }

  ///加载页面模版
  Future<List<IPageTemplate>> loadPageTemplates(Directory item) async {
    List<IPageTemplate> files = item.standard.templates;
    LogUtil.d('>>>>>>======loadPageTemplates ${files.length}');
    if (files.isEmpty) {
      return await item.standard.loadTemplates();
    }

    return files;
  }
}
