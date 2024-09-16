import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:orginone/components/common/list/index.dart';
import 'package:orginone/components/common/templates/gy_scaffold.dart';
import 'package:orginone/components/common/tip/toast.dart';
import 'package:orginone/dart/core/chat/session.dart';
import 'package:orginone/dart/core/public/enums.dart';
import 'package:orginone/main_base.dart';

class MessageForward extends StatefulWidget {
  final String msgBody;

  final String msgType;

  final VoidCallback? onSuccess;

  const MessageForward(
      {Key? key, required this.msgBody, required this.msgType, this.onSuccess})
      : super(key: key);

  @override
  State<MessageForward> createState() => _MessageForwardState();
}

class _MessageForwardState extends State<MessageForward> {
  late List<ISession> _list;
  late TextEditingController _searchController;
  late String searchText;

  @override
  void initState() {
    super.initState();
    _list = relationCtrl.provider.chatProvider?.chats ?? [];
    _searchController = TextEditingController();
    searchText = "";
  }

  @override
  Widget build(BuildContext context) {
    return GyScaffold(
      titleName: "发送给",
      body: Container(
          padding: EdgeInsets.symmetric(horizontal: 15.w),
          child: _buildList(
              context,
              searchText.isEmpty
                  ? _list
                  : _list
                      .where((element) => element.name.contains(searchText))
                      .toList())),
    );
  }

  Widget _buildList(BuildContext context, List<ISession> list) {
    return Column(children: [
      Container(
          margin: const EdgeInsets.all(0),
          padding: const EdgeInsets.all(0),
          child: TextField(
            decoration: const InputDecoration(
              hintText: '请输入搜索内容', // 默认提示文本
              // border: InputBorder, // 移除边框样式，可根据需要调整
            ),
            controller: _searchController,
            onChanged: _search,
          )),
      Expanded(
          child: ListWidget<ISession>(
        initDatas: list,
        getDatas: ([dynamic data]) {
          if (null == data) {
            return list ?? [];
          }
          return [];
        },
        onTap: (dynamic data, List children) {
          if (data is ISession) {
            _onTap(data);
          }
        },
      ))
    ]);
  }

  void _search(value) {
    setState(() {
      searchText = value;
    });
  }

  void _onTap(ISession item) {
    showCupertinoDialog(
      context: context,
      builder: (context) {
        return CupertinoAlertDialog(
          title: Text("发送给${item.chatdata.value.chatName}?"),
          actions: <Widget>[
            CupertinoDialogAction(
              child: const Text('取消'),
              onPressed: () {
                Navigator.pop(context);
              },
            ),
            CupertinoDialogAction(
              child: const Text('确定'),
              onPressed: () async {
                var msgType = MessageType.getType(widget.msgType);
                var success =
                    await item.sendMessage(msgType!, widget.msgBody, []);
                if (success) {
                  ToastUtils.showMsg(msg: "转发成功");
                }
                Navigator.pop(context, success);
              },
            ),
          ],
        );
      },
    ).then((success) {
      if (success ?? false) {
        if (widget.onSuccess != null) {
          widget.onSuccess!();
        }
      }
    });
  }
}
