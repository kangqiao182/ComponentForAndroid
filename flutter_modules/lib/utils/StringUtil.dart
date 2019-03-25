
class StringUtil {
  static bool isEmpty(String content) {
    return null == content || content.length == 0;
  }

  static bool isNotEmpty(String content) {
    return !isEmpty(content);
  }

  static bool isTrimEmpty(String content) {
    return null == content || content.trim().length == 0;
  }

  static bool isTrimNotEmpty(String content) {
    return !isTrimEmpty(content);
  }

  static final numRegExp = RegExp(r"^\d+$");
  static bool isNum(String content) {
    if (isNotEmpty(content)) {
      return numRegExp.hasMatch(content);
    }
    return false;
  }
}