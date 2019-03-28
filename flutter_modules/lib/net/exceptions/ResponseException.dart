
class ResponseException implements Exception{
  int code;
  String message;

  ResponseException(this.code, this.message);

  @override
  String toString() {
    return 'ResponseException{code: $code, message: $message}';
  }
}