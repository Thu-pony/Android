import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:orginone/components/common/templates/orginone_page_widget.dart';
import 'package:orginone/components/data_preview/chat/chat_box/chat_box.dart';
import 'package:orginone/components/data_preview/chat/info_item/info_item.dart';
import 'package:orginone/components/data_preview/chat/message_chat.dart';
import 'package:orginone/components/data_preview/chat/message_list.dart';
import 'package:orginone/components/empty/empty_chat.dart';
import 'package:orginone/dart/core/chat/session.dart';
import 'package:orginone/dart/core/target/base/target.dart';
import 'package:orginone/main_base.dart';

class ChatSessionPage extends OrginoneStatefulWidget {
  ChatSessionPage({super.key, super.data});
  @override
  State<StatefulWidget> createState() => _ChatSessionPageState();
}

class _ChatSessionPageState
    extends OrginoneStatefulState<ChatSessionPage, dynamic> {
  late ChatBoxController chatBoxCtrl;
  late MessageChatController chatCtrl;
  late String emitterKey;
  late dynamic _data;

  @override
  void initState() {
    super.initState();
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
    if (!Get.isRegistered<ChatBoxController>()) {
      chatBoxCtrl = ChatBoxController();
      Get.put(chatBoxCtrl);
    } else {
      chatBoxCtrl = Get.find<ChatBoxController>();
    }
    emitterKey = relationCtrl.subscribe((key, args) {
      if (null != data && null != relationCtrl.user) {
        // ToastUtils.showMsg(msg: "${data.id}");
        data = relationCtrl.findMemberChat(data.id);
        if (mounted && null != data) {
          // ToastUtils.showMsg(msg: "刷新对象：${data?.id}");
          data.clearCache();
          setState(() {});
        }
      }
    }, false);
  }

  @override
  void dispose() {
    super.dispose();
    relationCtrl.unsubscribe(emitterKey);
  }

  @override
  Widget buildWidget(BuildContext context, dynamic data) {
    // state.noReadCount = state.chat.noReadCount;
    ISession? session;
    if (data is ISession) {
      session = data;
    } else if (data is ITarget) {
      session = data.session;
    }

    return null != session
        ? GestureDetector(
            onTap: () {
              FocusScope.of(context).requestFocus(FocusNode());
              chatBoxCtrl.eventFire(context, InputEvent.clickBlank, session!);
            },
            child: Column(
              children: [
                Expanded(child: MessageList(chat: session)),
                ChatBox(chat: session, controller: chatBoxCtrl)
              ],
            ),
          )
        : const EmptyChat();
  }

  // @override
  // bool showMore(BuildContext context, dynamic data) {
  //   return true;
  // }

  // @override
  // void onMore(BuildContext context, dynamic data) {
  //   Get.toNamed(Routers.messageSetting, arguments: data);
  // }
}
