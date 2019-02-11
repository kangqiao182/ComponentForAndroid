import 'package:flutter/material.dart';

class KycLevel3Page extends StatefulWidget {

  final String title;
  KycLevel3Page({Key key,this.title}): super(key: key);

  @override
  _KycLevel3PageState createState() => _KycLevel3PageState();
}

class _KycLevel3PageState extends State<KycLevel3Page> {

  @override
  void initState() {
    super.initState();

  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: IconButton(icon: Icon(Icons.arrow_back_ios), onPressed: (){
          Navigator.of(context).deactivate();
        }),
        title: Text(widget.title),
      ),
      body: Container(
        padding: const EdgeInsets.all(15.0),
        child: new Column(
          children: <Widget>[
            new Container(height: 10.0),
          ],
        )
      ),
    );
  }
}