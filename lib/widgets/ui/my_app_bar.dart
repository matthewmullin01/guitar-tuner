import 'package:flutter/material.dart';

class MyAppBar extends StatelessWidget {
  final String title;
  MyAppBar({this.title = 'Title'});

  @override
  AppBar build(BuildContext context) {
    return AppBar(
      title: new Text(
        title,
        style: TextStyle(color: Colors.grey.shade700),
      ),
      centerTitle: true,
      backgroundColor: Theme.of(context).backgroundColor,
      elevation: 0.0,
      bottom: PreferredSize(
        preferredSize: Size(1000.0, 1.0),
        child: Container(
          height: 1.0,
          color: Colors.grey.shade300,
        ),
      ),
    );
  }
}
