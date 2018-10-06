import 'package:flutter/material.dart';
import 'package:guitar_tuner/widgets/note.dart';

class NotesContainer extends StatefulWidget {
  @override
  _NotesContainerState createState() => _NotesContainerState();
}

class _NotesContainerState extends State<NotesContainer> {
  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 20.00),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        children: <Widget>[
          Note(
            note: 'E',
          ),
          Note(
            note: 'A',
            active: true,
          ),
          Note(
            note: 'G',
          ),
          Note(
            note: 'B',
          ),
          Note(
            note: 'E',
          )
        ],
      ),
    );
  }
}
