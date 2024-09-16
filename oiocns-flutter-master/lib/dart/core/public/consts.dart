import 'package:orginone/dart/base/model.dart';
import 'package:orginone/dart/core/public/enums.dart';

// 异常消息常量
const unAuthorizedError = '抱歉,您没有权限操作.';
const isExistError = '抱歉,已存在请勿重复创建.';
const resultError = '抱歉,请求返回异常.';
const notFoundError = '抱歉,未找到该数据.';
const isJoinedError = '抱歉,您已加入该组织.';
const functionNotFoundError = '抱歉,未找到该方法.';

///资产共享云模块权限Id
enum OrgAuth {
  // 超管权限
  superAuthId("361356410044420096"),
  // 关系管理权限
  relationAuthId("361356410623234048"),
  // 物，标准等管理权限
  thingAuthId("361356410698731520"),
  // 办事管理权限
  workAuthId("361356410774228992");

  final String label;
  const OrgAuth(this.label);
}

enum TaskType {
  wait("待办事项"),
  done("已办事项"),
  altMe("抄送我的"),
  create("我发起的"),
  completed("已完结");

  final String label;
  const TaskType(this.label);
}

///数据存储集合名称
const storeCollName = {
  'workTask': 'work-task',
  'workInstance': 'work-instances',
  'chatMessage': 'chat-message',
  'transfer': 'standard-transfer',
};

///支持的单位类型
const companyTypes = [
  TargetType.company,
  TargetType.hospital,
  TargetType.university,
];

///支持的单位部门类型
const departmentTypes = [
  TargetType.college,
  TargetType.department,
  TargetType.office,
  TargetType.section,
  TargetType.major,
  TargetType.working,
  TargetType.research,
  TargetType.laboratory,
];

///支持的值类型
const valueType = [
  ValueType.number,
  ValueType.remark,
  ValueType.select,
  ValueType.species,
  ValueType.time,
  ValueType.target,
  ValueType.date,
  ValueType.file,
];

const targetDepartmentTypes = [
  TargetType.office,
  TargetType.section,
  TargetType.research,
  TargetType.laboratory,
  TargetType.jobCohort,
  TargetType.department,
];

const subDepartmentTypes = [
  TargetType.office,
  TargetType.section,
  TargetType.laboratory,
  TargetType.jobCohort,
  TargetType.research,
];
List<PopupMenuKey> createPopupMenuKey = [
  PopupMenuKey.createDir,
  PopupMenuKey.createApplication,
  PopupMenuKey.createSpecies,
  PopupMenuKey.createDict,
  PopupMenuKey.createAttr,
  PopupMenuKey.createThing,
  PopupMenuKey.createWork,
];

List<PopupMenuKey> defaultPopupMenuKey = [
  PopupMenuKey.upload,
  PopupMenuKey.openChat,
  PopupMenuKey.shareQr,
];

///表达弹框支持的类型
enum FormModalType {
  // ignore: constant_identifier_names
  New("New"),
  edit("Edit"),
  view("View");

  final String label;
  const FormModalType(this.label);
}

///用于获取全部的分页模型
final pageAll = PageModel(
  offset: 0,
  limit: (2 << 15) - 1, //ushort.max
  filter: '',
);

///通用状态信息Map
class Status {
  String color;
  String text;

  Status({required this.color, required this.text});
}

final Map<int, Status> StatusMap = {
  1: Status(color: 'blue', text: '待处理'),
  100: Status(color: 'green', text: '已同意'),
  200: Status(color: 'red', text: '已拒绝'),
  102: Status(color: 'green', text: '已发货'),
  220: Status(color: 'green', text: '已取消'),
};

var ShareIdSet = <String, ShareIcon>{};

/// 常量
class Constants {
// wp 服务器

  // 本地存储key language_code
  static const storageLanguageCode = 'language_code';
  //主题
  static const storageThemeCode = 'theme_code';

  static const storageisFirstOpen = 'first_open'; // 首次打开

  // AES
  // static const aesKey = 'aH5aH5bG0dC6aA3oN0cK4aU5jU6aK2lN'; //加密 key 32 位
  // static const aesIV = 'hK6eB4aE1aF3gH5q'; // 加密向量 16 位
  static const aesKey = ''; // EnvConfig.pwdEncryptKey; //加密 key 32 位
  static const aesIV = 'hK6eB4aE1aF3gH5q'; // 加密向量 16 位

  static const baseUrlKey = 'app-base-url'; // 基础路径字段名
  static const sessionUser = 'sessionUser'; // 用户会话
  static const loginStatus = "loginStatus"; //登陆状态
  static const account = 'account'; //
  static const appTokenKey =
      'X-Auth-Token'; //X-Auth-Token Authorization appToken字段名(本地存储，以及dio请求头均使用到此字段名称，不能随意改动)
  static const appTokenGenerationTime = 'X-Auth-Token-time'; //token生成时间
  static const userInfoKey = 'app-user-info'; // 用户基础信息字段名
  static const userNamePassword = 'app-user-name-password'; // 用户名密码
  static const userResKey = 'app-user-res'; // 用户权限资源表字段名
  static const warehouse = 'app-user-warehouse'; // 当前使用仓库
  static const carNumber = 'app-car-number'; // 最后一次使用的车牌号

  static const accountRegex = r'(^1[3|4|5|7|8|9]\d{9}$)|(^09\d{8}$)'; //手机账号格式验证
  static const passwordRegex =
      r'(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{6,15}'; //密码格式
  static const realNameRegex = r'^[\u4e00-\u9fa5]{2,8}$'; //用户姓名格式
}
