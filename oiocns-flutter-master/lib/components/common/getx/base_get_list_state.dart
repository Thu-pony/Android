import 'package:easy_refresh/easy_refresh.dart';
import 'package:get/get.dart';

import 'base_get_state.dart';

enum LoadStatusX {
  loading,
  error,
  success,
  empty,
}

@Deprecated("待废弃")
abstract class BaseGetListState<T> extends BaseGetState {
  var dataList = <T>[].obs;

  var loadStatus = LoadStatusX.loading.obs;

  int page = 0;

  EasyRefreshController refreshController = EasyRefreshController();

  var isSuccess = false.obs;

  var isLoading = true.obs;
}
