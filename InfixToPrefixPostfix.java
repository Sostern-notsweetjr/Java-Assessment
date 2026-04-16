import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class InfixToPrefixPostfix {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter an infix expression: ");
        String infix = scanner.nextLine();

        try {
            String postfix = infixToPostfix(infix);
            String prefix = postfixToPrefix(postfix);

            System.out.println("Infix   : " + infix);
            System.out.println("Postfix : " + postfix);
            System.out.println("Prefix  : " + prefix);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }

    public static String infixToPostfix(String expression) {
        List<String> tokens = tokenize(expression);
        Stack<String> operators = new Stack<>();
        List<String> output = new ArrayList<>();

        for (String token : tokens) {
            if (isOperand(token)) {
                output.add(token);
            } else if ("(".equals(token)) {
                operators.push(token);
            } else if (")".equals(token)) {
                while (!operators.isEmpty() && !"(".equals(operators.peek())) {
                    output.add(operators.pop());
                }

                if (operators.isEmpty()) {
                    throw new IllegalArgumentException("Mismatched parentheses in expression.");
                }

                operators.pop();
            } else if (isOperator(token)) {
                while (!operators.isEmpty()
                        && isOperator(operators.peek())
                        && shouldPopOperator(token, operators.peek())) {
                    output.add(operators.pop());
                }
                operators.push(token);
            } else {
                throw new IllegalArgumentException("Invalid token: " + token);
            }
        }

        while (!operators.isEmpty()) {
            String top = operators.pop();

            if ("(".equals(top) || ")".equals(top)) {
                throw new IllegalArgumentException("Mismatched parentheses in expression.");
            }

            output.add(top);
        }

        return String.join(" ", output);
    }

    public static String postfixToPrefix(String postfix) {
        Stack<String> stack = new Stack<>();

        for (String token : postfix.split("\\s+")) {
            if (token.isEmpty()) {
                continue;
            }

            if (isOperand(token)) {
                stack.push(token);
            } else if (isOperator(token)) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Invalid postfix expression.");
                }

                String right = stack.pop();
                String left = stack.pop();
                stack.push(token + " " + left + " " + right);
            } else {
                throw new IllegalArgumentException("Invalid token in postfix expression: " + token);
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression.");
        }

        return stack.pop();
    }

    private static List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        StringBuilder operand = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (Character.isWhitespace(ch)) {
                addOperandIfPresent(tokens, operand);
            } else if (Character.isLetterOrDigit(ch)) {
                operand.append(ch);
            } else if (isOperatorChar(ch) || ch == '(' || ch == ')') {
                addOperandIfPresent(tokens, operand);
                tokens.add(String.valueOf(ch));
            } else {
                throw new IllegalArgumentException("Unsupported character: " + ch);
            }
        }

        addOperandIfPresent(tokens, operand);
        return tokens;
    }

    private static void addOperandIfPresent(List<String> tokens, StringBuilder operand) {
        if (operand.length() > 0) {
            tokens.add(operand.toString());
            operand.setLength(0);
        }
    }

    private static boolean isOperand(String token) {
        for (int i = 0; i < token.length(); i++) {
            if (!Character.isLetterOrDigit(token.charAt(i))) {
                return false;
            }
        }
        return !token.isEmpty();
    }

    private static boolean isOperator(String token) {
        return "+".equals(token)
                || "-".equals(token)
                || "*".equals(token)
                || "/".equals(token)
                || "^".equals(token);
    }

    private static boolean isOperatorChar(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^';
    }

    private static int precedence(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;
            default:
                return -1;
        }
    }

    private static boolean isRightAssociative(String operator) {
        return "^".equals(operator);
    }

    private static boolean shouldPopOperator(String current, String top) {
        if (isRightAssociative(current)) {
            return precedence(current) < precedence(top);
        }
        return precedence(current) <= precedence(top);
    }
}
