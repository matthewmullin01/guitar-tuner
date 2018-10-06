import 'dart:async';
import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:guitar_tuner/widgets/indicator.dart';
import 'package:guitar_tuner/widgets/notes_container.dart';
import 'package:guitar_tuner/widgets/ui/my_app_bar.dart';

import 'package:simple_permissions/simple_permissions.dart';

class MyHomePage extends StatefulWidget {
  final String title;
  MyHomePage({Key key, this.title}) : super(key: key);

  @override
  _MyHomePageState createState() => new _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const platform = const MethodChannel('matthew.mullin.io/audio');
  static const stream = const EventChannel('matthew.mullin.io/stream');

  StreamSubscription _audioSubscription;

  double _amplitude;

  void _listenToAudio() {
    if (_audioSubscription == null) {
      _audioSubscription =
          // stream.receiveBroadcastStream().listen(_remapAmplitude);
          stream.receiveBroadcastStream().listen(_updateAmplitude);
    }
  }

  void _remapAmplitude(dynamic amplitude) {}

  void _updateAmplitude(double amplitude) {
    // print("Amplitude $amplitude");
    print(amplitude.runtimeType.toString());
    setState(() => _amplitude = num.parse(amplitude.toStringAsFixed(3)));
  }

  initState() {
    super.initState();
    setupRecording();
    _listenToAudio();
  }

  void setupRecording() async {
    await _getPermissions();
    await _startRecording();
    // await _getAmplitude();
    // await _stopRecording();
  }

  Future<void> _getPermissions() async {
    PermissionStatus result =
        await SimplePermissions.requestPermission(Permission.RecordAudio);
    await SimplePermissions.requestPermission(Permission.ReadPhoneState);
    print("request :" + result.toString());
    return;
  }

  Future<Null> _startRecording() async {
    try {
      final String result = await platform.invokeMethod('start_recording');
      print(result);
    } on PlatformException catch (e) {
      print("Failed to record: '${e.message}'.");
    }
    return;
  }

  Future<Null> _stopRecording() async {
    try {
      final String result = await platform.invokeMethod('stop_recording');
      print(result);
    } on PlatformException catch (e) {
      print("Failed to record: '${e.message}'.");
    }
    return;
  }

  @override
  Widget build(BuildContext context) {
    Widget _buildTaget(double radius) {
      return Container(
        width: radius,
        height: radius,
        decoration: BoxDecoration(
          shape: BoxShape.circle,
          color: Colors.transparent,
          border: Border.all(
            color: Colors.black.withOpacity(0.2),
          ),
        ),
      );
    }

    Widget _buildActualTaget(double radius) {
      return Container(
        width: radius,
        height: radius,
        decoration: BoxDecoration(
          shape: BoxShape.circle,
          color: Colors.transparent,
          border: Border.all(
            color: Theme.of(context).accentColor.withOpacity(0.4),
            width: 2.0,
          ),
        ),
      );
    }

    Widget _buildBlur(double radius) {
      return BackdropFilter(
        filter: ImageFilter.blur(sigmaX: 1.5, sigmaY: 1.5),
        // child: SizedBox.expand(
        child: Container(
          width: radius,
          height: radius,
          decoration: BoxDecoration(
            shape: BoxShape.circle,
            color: Colors.white.withOpacity(0.5),
          ),
        ),
      );
    }

    return Scaffold(
      appBar: PreferredSize(
        preferredSize: Size(0.0, 58.0),
        child: MyAppBar(
          title: widget.title,
        ),
      ),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Text(
            '$_amplitude', // Temporary
          ),
          Expanded(
            child: Stack(
              alignment: Alignment.center,
              children: <Widget>[
                _buildTaget(60.00),
                _buildTaget(100.00),
                _buildTaget(140.00),
                _buildTaget(180.00),
                _buildTaget(260.00),
                _buildBlur(260.00),
                _buildActualTaget(220.00),
                Indicator(),
              ],
            ),
          ),
          NotesContainer(),
        ],
      ),
      backgroundColor: Theme.of(context).backgroundColor,
    );
  }
}
