package util;

import java.util.regex.Pattern;

public final class Validaciones {
    private static final Pattern NOMBRE = Pattern.compile("^\\p{L}[\\p{L} .'-]*$");
    private static final Pattern TELEFONO = Pattern.compile("^\\+?\\d{8,15}$");
    private static final Pattern CORREO = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    private Validaciones() { }

    public static String errorTexto(String valor, String etiqueta, int min, int max) {
        if (valor == null || valor.isBlank()) {
            return "Debe ingresar " + etiqueta + ".";
        }
        if (valor.trim().length() < min) {
            return "La " + etiqueta.replaceFirst("^(el|la) ", "") + " debe tener al menos " + min + " caracteres.";
        }
        if (valor.trim().length() > max) {
            return "Se permiten como máximo " + max + " caracteres en " + etiqueta + ".";
        }
        return null;
    }

    public static String errorNombre(String valor, String etiqueta, int max) {
        String error = errorTexto(valor, etiqueta, 2, max);
        if (error != null) {
            return error;
        }
        if (!NOMBRE.matcher(valor.trim()).matches()) {
            return "Formato inválido en " + etiqueta + ": use solo letras, espacios, puntos, apóstrofes o guiones.";
        }
        return null;
    }

    public static String normalizarTelefono(String valor) {
        return valor == null ? "" : valor.replaceAll("[\\s-]", "");
    }

    public static String errorTelefono(String valor) {
        String limpio = normalizarTelefono(valor);
        if (limpio.isEmpty()) {
            return "Debe ingresar el teléfono.";
        }
        if (!TELEFONO.matcher(limpio).matches()) {
            return "Teléfono inválido: use entre 8 y 15 dígitos (puede iniciar con +).";
        }
        return null;
    }

    public static String errorCorreo(String valor, int max) {
        if (valor == null || valor.isBlank()) {
            return "Debe ingresar el correo electrónico.";
        }
        String limpio = valor.trim();
        if (limpio.length() > max) {
            return "El correo no puede superar los " + max + " caracteres.";
        }
        if (!CORREO.matcher(limpio).matches()) {
            return "Correo inválido: use el formato usuario@dominio.com.";
        }
        return null;
    }
}