import com.google.gson.Gson;
// import com.dampcake.bencode.Bencode; - available if you need it!

public class Main {
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        try {
            executeCommand(args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void executeCommand(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("No command provided");
        }

        String command = args[0];
        if ("decode".equals(command)) {
            handleDecodeCommand(args);
        } else {
            System.out.println("Unknown command " + command);
        }
    }

    private static void handleDecodeCommand(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("No bencoded string provided");
        }
        String bencodedValue = args[1];
        Object decoded;
        decoded = decodeBencode(bencodedValue);
        System.out.println(gson.toJson(decoded));
    }

    static Object decodeBencode(String bencodedString) {
        if (Character.isDigit(bencodedString.charAt(0))) {
            return decodeString(bencodedString);
        } else if (bencodedString.startsWith("i")) {
            return decodeInteger(bencodedString);
        } else {
            throw new RuntimeException("Only strings and integers are supported at the moment");
        }

    }

    private static long decodeInteger(String bencodedString) {
        if (!bencodedString.endsWith("e")) {
            throw new RuntimeException("Invalid integer format");
        }

        try {
            return Long.parseLong(bencodedString.substring(1, bencodedString.length() - 1));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid integer value");
        }
    }

    private static String decodeString(String bencodedString) {
        if (Character.isDigit(bencodedString.charAt(0))) {
            int firstColonIndex = 0;
            for (int i = 0; i < bencodedString.length(); i++) {
                if (bencodedString.charAt(i) == ':') {
                    firstColonIndex = i;
                    break;
                }
            }
            int length = Integer.parseInt(bencodedString.substring(0, firstColonIndex));
            return bencodedString.substring(firstColonIndex + 1, firstColonIndex + 1 + length);
        } else {
            throw new RuntimeException("Only strings are supported at the moment");
        }
    }

}
