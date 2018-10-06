import 'package:flutter/material.dart';
import 'package:guitar_tuner/screens/home.screen.dart';

void main() => runApp(new MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: 'Flutter Demo',
      theme: new ThemeData(
        primaryColor: Color.fromARGB(255, 228, 89, 116),
        accentColor: Color.fromARGB(255, 128, 130, 229),
        backgroundColor: Color.fromARGB(255, 251, 251, 255),
        fontFamily: 'Raleway',
      ),
      home: new MyHomePage(title: 'Tuner'),
    );
  }
}
