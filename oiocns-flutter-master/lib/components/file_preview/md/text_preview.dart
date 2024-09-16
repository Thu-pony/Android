import 'package:flutter/material.dart';
import 'package:orginone/components/common/templates/orginone_page_widget.dart';

class TextPreview extends OrginoneStatelessWidget<String> {
  TextPreview({super.key, super.data});

  @override
  Widget buildWidget(BuildContext context, String data) {
    return Container(
      margin: const EdgeInsets.only(top: 1),
      padding: const EdgeInsets.all(10),
      color: Colors.white,
      child: SingleChildScrollView(
        child: Text(data),
      ),
    );
  }
}
