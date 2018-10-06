import 'package:flutter/material.dart';

class Note extends StatefulWidget {
  final String note;
  final bool active;

  Note({this.note = 'A', this.active = false});

  @override
  _NoteState createState() => _NoteState();
}

class _NoteState extends State<Note> {
  Widget _buildActiveNote() {
    return Column(
      children: <Widget>[
        Container(
          width: 40.00,
          height: 40.00,
          decoration: BoxDecoration(
            shape: BoxShape.circle,
            color: Theme.of(context).accentColor,
          ),
          child: Center(
            child: Text(
              widget.note,
              style: TextStyle(color: Colors.white, fontSize: 24.00),
            ),
          ),
        ),
        Container(
          color: Theme.of(context).accentColor,
          height: 50.00,
          width: 5.00,
        ),
      ],
    );
  }

  Widget _buildInActiveNote() {
    return Column(
      children: <Widget>[
        Container(
          width: 40.00,
          height: 40.00,
          decoration: BoxDecoration(
            shape: BoxShape.circle,
            color: Colors.transparent,
          ),
          child: Center(
            child: Text(
              widget.note,
              style: TextStyle(color: Colors.grey.shade400, fontSize: 24.00),
            ),
          ),
        ),
        Container(
          color: Colors.transparent,
          height: 50.00,
          width: 5.00,
        )
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    return widget.active ? _buildActiveNote() : _buildInActiveNote();
  }
}
