#SOSTERN HAMULAMBO MWENDA 
#202409694
#ICT 202 ASSESMENT
# Infix to Postfix and Prefix

## Workflow

1. Start the program.
2. Read the infix expression from the user.
3. Break the expression into tokens such as operands, operators, and parentheses.
4. Convert the infix expression to postfix using a stack:
   - Send operands directly to the output.
   - Push `(` onto the stack.
   - When `)` is found, pop operators until `(` is reached.
   - For an operator, pop higher-precedence operators from the stack first, then push the current operator.
5. Convert the postfix expression to prefix using another stack:
   - Push operands onto the stack.
   - When an operator is found, pop the top two operands/expressions.
   - Form a new prefix string as `operator left right`.
   - Push the new string back onto the stack.
6. Display the postfix and prefix expressions.
7. End the program.

## Pseudocode

```text
BEGIN
    INPUT infixExpression

    postfix = infixToPostfix(infixExpression)
    prefix = postfixToPrefix(postfix)

    PRINT postfix
    PRINT prefix
END

FUNCTION infixToPostfix(expression)
    create empty stack operators
    create empty list output
    tokens = tokenize(expression)

    FOR each token in tokens
        IF token is operand
            add token to output
        ELSE IF token is "("
            push token onto operators
        ELSE IF token is ")"
            WHILE top of operators is not "("
                pop from operators and add to output
            END WHILE
            pop "(" from operators
        ELSE IF token is operator
            WHILE operators is not empty
              AND top of operators is an operator
              AND top should be processed first
                pop from operators and add to output
            END WHILE
            push token onto operators
        END IF
    END FOR

    WHILE operators is not empty
        pop from operators and add to output
    END WHILE

    RETURN output joined with spaces
END FUNCTION

FUNCTION postfixToPrefix(postfix)
    create empty stack expressions

    FOR each token in postfix
        IF token is operand
            push token onto expressions
        ELSE
            right = pop from expressions
            left = pop from expressions
            newExpression = token + " " + left + " " + right
            push newExpression onto expressions
        END IF
    END FOR

    RETURN top of expressions
END FUNCTION
```
