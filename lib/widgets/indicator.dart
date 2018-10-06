import 'package:flutter/material.dart';

class Indicator extends StatefulWidget {
  @override
  _IndicatorState createState() => _IndicatorState();
}

class _IndicatorState extends State<Indicator> {
  @override
  Widget build(BuildContext context) {
    return Container(
      width: 150.00,
      height: 150.00,
      decoration: BoxDecoration(
          shape: BoxShape.circle,
          // color: Theme.of(context).primaryColor,
          gradient: LinearGradient(
            colors: [
              Color(0xFFEC5D73),
              Color(0xFFFF9A86),
            ],
            begin: Alignment.bottomCenter,
            end: Alignment.topCenter,
          ),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withOpacity(0.4),
              blurRadius: 10.0,
            ),
          ]),
      child: Center(
        child: Text(
          'G',
          style: TextStyle(color: Colors.white, fontSize: 90.00),
        ),
      ),
    );
  }
}
