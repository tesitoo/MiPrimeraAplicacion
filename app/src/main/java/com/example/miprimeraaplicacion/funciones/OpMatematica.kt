package com.example.miprimeraaplicacion.funciones

object OpMatematica {

    fun dividirValores(valor1:Int, valor2:Int): String{
        try {
            var calculo_interno:Int =  valor1 / valor2
            return  calculo_interno.toString()
        }catch (e: ArithmeticException){
            return "404"
        }finally {
            println("SE HA CONTROLADO EL ERROR DIVISION BY ZERO")
        }

    }

}