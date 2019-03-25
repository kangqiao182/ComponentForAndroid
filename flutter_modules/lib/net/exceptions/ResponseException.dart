
class ResponseException implements Exception{
  int code;
  String message;

  ResponseException(this.code, this.message);
}