
package com.mycompany.proyecto1;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class Proyecto1 {

    // Método para evaluar la expresión matemática y calcular su resultado
    public static int evaluar(String expresion, Map<Character, Integer> variables) {
        char[] chars = expresion.toCharArray();

        Stack<Integer> stackNumeros = new Stack<>();
        Stack<Character> stackOperadores = new Stack<>();

        boolean unario = true; // Bandera para indicar si el operador es unario

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ' ') {
                continue; // Saltar espacios en blanco
            }

            if (Character.isDigit(chars[i])) {
                // Si es un dígito, construir el número y agregarlo a la pila de números
                StringBuilder sbuf = new StringBuilder();
                while (i < chars.length && (Character.isDigit(chars[i]) || chars[i] == '.')) {
                    sbuf.append(chars[i++]);
                }
                i--; // Retroceder para volver a leer el siguiente carácter
                stackNumeros.push(Integer.parseInt(sbuf.toString()));
                unario = false; // El número no es un operador unario
            } else if (chars[i] == '(') {
                // Si es un paréntesis de apertura, agregarlo a la pila de operadores
                stackOperadores.push(chars[i]);
                unario = true; // El siguiente operador puede ser unario
            } else if (chars[i] == ')') {
                // Si es un paréntesis de cierre, evaluar las operaciones dentro del paréntesis
                while (!stackOperadores.isEmpty() && stackOperadores.peek() != '(') {
                    stackNumeros.push(operar(stackOperadores.pop(), stackNumeros.pop(), stackNumeros.pop()));
                }
                stackOperadores.pop(); // Eliminar el paréntesis de apertura
                unario = false; // El paréntesis cierra una expresión, no puede ser seguido por un operador unario
            } else if (chars[i] == '+' || chars[i] == '-' || chars[i] == '*' || chars[i] == '/') {
                // Verificar si el operador es unario
                boolean esUnario = unario && (chars[i] == '+' || chars[i] == '-');
                // Evaluar las operaciones de acuerdo a la precedencia de operadores
                while (!stackOperadores.isEmpty() && (!esUnario && tienePrecedencia(chars[i], stackOperadores.peek()))) {
                    stackNumeros.push(operar(stackOperadores.pop(), stackNumeros.pop(), stackNumeros.pop()));
                }
                stackOperadores.push(chars[i]); // Agregar el operador a la pila de operadores
                unario = esUnario; // Actualizar la bandera de operador unario
            } else if (Character.isAlphabetic(chars[i])) {
                // Si es una variable, obtener su valor y agregarlo a la pila de números
                if (!variables.containsKey(chars[i])) {
                    System.out.print("Ingrese el valor para " + chars[i] + ": ");
                    Scanner scanner = new Scanner(System.in);
                    int valor = scanner.nextInt();
                    scanner.close();
                    variables.put(chars[i], valor);
                }
                stackNumeros.push(variables.get(chars[i]));
                unario = false; // Una variable no puede ser seguida por un operador unario
            }
        }

        // Evaluar las operaciones restantes en la pila de operadores y números
        while (!stackOperadores.isEmpty()) {
            stackNumeros.push(operar(stackOperadores.pop(), stackNumeros.pop(), stackNumeros.pop()));
        }

        // Devolver el resultado final
        return stackNumeros.pop();
    }

    // Método para verificar la precedencia de operadores
    public static boolean tienePrecedencia(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return false;
        } else {
            return true;
        }
    }

    // Método para realizar operaciones aritméticas
    public static int operar(char op, int b, int a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new UnsupportedOperationException("No se puede dividir por cero");
                }
                return a / b;
        }
        return 0;
    }

    // Método principal
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la expresión matemática con variables: ");
        String expresion = scanner.nextLine();

        // Mapa para almacenar los valores de las variables
        Map<Character, Integer> variables = new HashMap<>();
        for (char c : expresion.toCharArray()) {
            // Solicitar al usuario que ingrese el valor de cada variable encontrada en la expresión
            if (Character.isAlphabetic(c) && !variables.containsKey(c)) {
                System.out.print("Ingrese el valor para " + c + ": ");
                int valor = scanner.nextInt();
                variables

.put(c, valor);
            }
        }

        // Convertimos casos como 5(10+2) a 5*(10+2)
        expresion = expresion.replaceAll("(?<=\\w)(?=\\()", "*");

        // Mostrar la expresión matemática con los valores ingresados
        System.out.println("Expresión matemática con valores ingresados:");
        StringBuilder expresionConValores = new StringBuilder();
        for (char c : expresion.toCharArray()) {
            if (Character.isAlphabetic(c)) {
                expresionConValores.append(variables.get(c));
            } else {
                expresionConValores.append(c);
            }
        }
        System.out.println(expresionConValores.toString());

        // Evaluar la expresión y mostrar el resultado
        int resultado = evaluar(expresion, variables);
        System.out.println("El resultado de la expresión es: " + resultado);
    }
}