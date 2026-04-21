import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Project2Server {

    public static final int SCALE = 8;

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        int port;

        System.out.print("Enter port number: ");
        port = keyboard.nextInt();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                handleClient(clientSocket);
                System.out.println("Client disconnected.");
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }

        keyboard.close();
    }

    public static void handleClient(Socket clientSocket) {
        try (
            Socket autoClose = clientSocket;
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream())
            );
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String operation = in.readLine();
            String num1 = in.readLine();
            String num2 = in.readLine();

            String result = processRequest(operation, num1, num2);
            out.println(result);

        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }

    public static String processRequest(String operation, String num1, String num2) {
        if (operation == null) {
            return "Unknown operation";
        }

        operation = operation.trim().toLowerCase();

        if (!operation.equals("add") &&
            !operation.equals("sub") &&
            !operation.equals("mult") &&
            !operation.equals("shuffle")) {
            return "Unknown operation";
        }

        boolean firstValid = isBigDecimalNumber(num1);
        boolean secondValid = isBigDecimalNumber(num2);

        if (!firstValid && !secondValid) {
            return "Both values: not numbers";
        }
        if (!firstValid) {
            return "First value: not a number";
        }
        if (!secondValid) {
            return "Second value: not a number";
        }

        BigDecimal bd1 = new BigDecimal(num1);
        BigDecimal bd2 = new BigDecimal(num2);

        switch (operation) {
            case "add":
                return bd1.add(bd2)
                        .setScale(SCALE, RoundingMode.HALF_UP)
                        .toPlainString();

            case "sub":
                return bd1.subtract(bd2)
                        .setScale(SCALE, RoundingMode.HALF_UP)
                        .toPlainString();

            case "mult":
                return bd1.multiply(bd2)
                        .setScale(SCALE, RoundingMode.HALF_UP)
                        .toPlainString();

            case "shuffle":
                return shuffleNumbers(num1, num2);

            default:
                return "Unknown operation";
        }
    }

    public static boolean isBigDecimalNumber(String num) {
        if (num == null || num.trim().isEmpty()) {
            return false;
        }

        num = num.trim();

        return Pattern.matches("-?\\d+(\\.\\d+)?", num);
    }

    public static String shuffleNumbers(String num1, String num2) {
        ParsedNumber p1 = parseNumber(num1);
        ParsedNumber p2 = parseNumber(num2);

        int maxIntLen = Math.max(p1.integerPart.length(), p2.integerPart.length());
        int maxFracLen = Math.max(p1.fractionalPart.length(), p2.fractionalPart.length());

        String int1 = leftPadZeros(p1.integerPart, maxIntLen);
        String int2 = leftPadZeros(p2.integerPart, maxIntLen);
        String frac1 = rightPadZeros(p1.fractionalPart, maxFracLen);
        String frac2 = rightPadZeros(p2.fractionalPart, maxFracLen);

        StringBuilder shuffledInt = new StringBuilder();
        StringBuilder shuffledFrac = new StringBuilder();

        for (int i = 0; i < maxIntLen; i++) {
            shuffledInt.append(int1.charAt(i));
            shuffledInt.append(int2.charAt(i));
        }

        for (int i = 0; i < maxFracLen; i++) {
            shuffledFrac.append(frac1.charAt(i));
            shuffledFrac.append(frac2.charAt(i));
        }

        String intResult = removeLeadingZeros(shuffledInt.toString());
        String fracResult = removeTrailingZeros(shuffledFrac.toString());

        if (intResult.isEmpty()) {
            intResult = "0";
        }

        if (fracResult.isEmpty()) {
            return intResult;
        } else {
            return intResult + "." + fracResult;
        }
    }

    public static ParsedNumber parseNumber(String num) {
        String integerPart;
        String fractionalPart;

        if (num.startsWith("-")) {
            num = num.substring(1);
        }

        if (num.contains(".")) {
            String[] parts = num.split("\\.", -1);
            integerPart = parts[0];
            fractionalPart = parts[1];
        } else {
            integerPart = num;
            fractionalPart = "";
        }

        return new ParsedNumber(integerPart, fractionalPart);
    }

    public static String leftPadZeros(String s, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = s.length(); i < length; i++) {
            sb.append('0');
        }
        sb.append(s);
        return sb.toString();
    }

    public static String rightPadZeros(String s, int length) {
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < length) {
            sb.append('0');
        }
        return sb.toString();
    }

    public static String removeLeadingZeros(String s) {
        int i = 0;
        while (i < s.length() - 1 && s.charAt(i) == '0') {
            i++;
        }
        return s.substring(i);
    }

    public static String removeTrailingZeros(String s) {
        int i = s.length() - 1;
        while (i >= 0 && s.charAt(i) == '0') {
            i--;
        }
        if (i < 0) {
            return "";
        }
        return s.substring(0, i + 1);
    }

    static class ParsedNumber {
        String integerPart;
        String fractionalPart;

        ParsedNumber(String integerPart, String fractionalPart) {
            this.integerPart = integerPart;
            this.fractionalPart = fractionalPart;
        }
    }
}
