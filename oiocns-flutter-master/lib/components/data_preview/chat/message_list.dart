import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:orginone/dart/core/chat/message.dart';
import 'package:orginone/dart/core/chat/session.dart';
import 'package:orginone/utils/date_util.dart';
import 'package:orginone/config/theme/unified_style.dart';
import 'package:scrollable_positioned_list/scrollable_positioned_list.dart';

import 'info_item/info_item.dart';
import 'message_chat.dart';

class MessageList extends StatefulWidget {
  final ISession? chat;
  const MessageList({Key? key, this.chat}) : super(key: key);

  @override
  State<MessageList> createState() => _MessageListState();
}

class _MessageListState extends State<MessageList> {
  MessageChatController get controller => Get.find();

  ISession? get chat => widget.chat ?? controller.state.chat;
  bool isBuilded = false;

  @override
  void dispose() {
    super.dispose();
    chat?.unMessage();
  }

  @override
  void initState() {
    super.initState();
    MessageChatController chatCtrl;
    if (!Get.isRegistered<MessageChatController>()) {
      chatCtrl = MessageChatController();
      chatCtrl.context = context;
      Get.put(chatCtrl);
    } else {
      chatCtrl = Get.find<MessageChatController>();
      chatCtrl.context = context;
    }
    if (!Get.isRegistered<PlayController>()) {
      PlayController playCtrl = PlayController();
      Get.lazyPut(() => playCtrl);
    }
    chat?.onMessage((messages) => null).then((value) {
      if (!isBuilded) {
        if (mounted) {
          setState(() {});
        }
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    isBuilded = true;
    return null != chat
        ? RefreshIndicator(
            onRefresh: () => chat!.moreMessage(),
            child: Obx(() {
              return ScrollablePositionedList.builder(
                padding: EdgeInsets.only(left: 10.w, right: 10.w),
                shrinkWrap: true,
                key: controller.state.scrollKey,
                reverse: true,
                physics: const ClampingScrollPhysics(),
                itemScrollController: controller.state.itemScrollController,
                addAutomaticKeepAlives: true,
                addRepaintBoundaries: true,
                itemCount: chat!.messages.length,
                itemBuilder: (BuildContext context, int index) {
                  return _item(index);
                },
              );
            }))
        : Container();
  }

  Widget _item(int index) {
    ///兼容异常问题
    //     RangeError (index): Invalid value: Valid value range is empty: 0
    // #0 List.[] (dart:core-patch/growable_array.dart:264)
    // #1 RxList.[] (package:get/get_rx/src/rx_types/rx_iterables/rx_list.dart:60)
    // #2 _MessageListState._item (package:orginone/pages/chat/widgets/message_list.dart:84)
    // #3 _MessageListState.build.. (package:orginone/pages/chat/widgets/message_list.dart:76)
    // #4 _PositionedListState._buildItem (package:scrollable_positioned_list/src/positioned_list.dart:252)
    // #5 _PositionedListState.build. (package:scrollable_positioned_list/src/positioned_list.dart:206)
    if (chat!.messages.length < index && chat!.messages.isEmpty) {
      return Container();
    }
    IMessage msg = chat!.messages[index];
    Widget currentWidget = DetailItemWidget(
      msg: msg,
      chat: chat!,
      key: ObjectKey(msg.metadata),
    );
    var time = _time(msg.createTime);
    var item = Column(children: [currentWidget]);
    if (index == 0) {
      item.children.add(Container(margin: EdgeInsets.only(bottom: 5.h)));
    }
    if (index == chat!.messages.length - 1) {
      item.children.insert(0, time);
      return item;
    } else {
      IMessage pre = chat!.messages[index + 1];
      var curCreateTime = DateTime.parse(msg.createTime);
      var preCreateTime = DateTime.parse(pre.createTime);
      var difference = curCreateTime.difference(preCreateTime);
      if (difference.inSeconds > 60 * 3) {
        item.children.insert(0, time);
        return item;
      }
      return item;
    }
  }

  Widget _time(String dateTime) {
    var content = CustomDateUtil.getDetailTime(DateTime.parse(dateTime));
    return Container(
      alignment: Alignment.center,
      margin: EdgeInsets.only(top: 10.h, bottom: 10.h),
      child: Text(content, style: XFonts.size16Black9),
    );
  }
}
