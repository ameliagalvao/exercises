"""
suponha que você possui R$10.000 iniciais, consegue aportar R$1.000 por mês e obtém um rendimento de 0,54% ao mês.
Por simplicidade, suponha que você faz o aporte após o rendimento no período acontecer.
No primeiro mês, terá R$10.000 + 0,54% deste valor = R$10.054,00. E, com o novo aporte, R$11.054,00.
No segundo mês, o valor inicial é de R$11.054,00. Ele rende 0,54%, totalizando R$11.113,69. Com o novo aporte,
R$12.113,69, e assim sucessivamente.
Ao final de 120 meses, você terá o montante final de R$187.303,05.
Crie um programa que ponha a hipótese de Einstein à prova. Em uma função, receba, como entrada, um montante
financeiro inicial, um percentual de rendimento por período, um valor de aporte para cada período e uma
quantidade de períodos.
Crie uma função que exiba um gráfico da evolução do valor acumulado, tendo como eixo das abscissas
(horizontal) o número de períodos e, no eixo das ordenadas (vertical), o valor acumulado, em reais.
"""


import matplotlib.pyplot as plt

valores_por_periodo = []
numero_periodos = []


def calcular_juros_compostos(valor_investido, rendimento_periodo, aporte_periodo, total_periodos):
    periodo = 1
    while periodo <= total_periodos:
        valor_investido = round(valor_investido +
                                (rendimento_periodo * (valor_investido / 100)), 2)
        valor_investido += aporte_periodo
        valores_por_periodo.append(valor_investido)
        numero_periodos.append(periodo)
        print(
            f'Após {periodo} período(s), o montante será de R${valor_investido}')
        periodo += 1
    print('\nO gráfico da evolução do valor acumulado é o seguinte:\n')
    gerar_grafico_juros()


def gerar_grafico_juros():
    eixo_x = numero_periodos
    eixo_y = valores_por_periodo

    plt.plot(eixo_x, eixo_y, 'o')
    plt.xlabel('Períodos')
    plt.ylabel('Valor acumulado em R$ por período')

    plt.show()


valor_investido = float(input('Insira aqui o valor inicial em R$: '))
rendimento_periodo = float(
    input('Insira o percentual de rendimento por período (sem o símbolo da %): '))
aporte_periodo = float(input('Insira o aporte por período em R$: '))
total_periodos = int(input('Insira o total de períodos: '))

calcular_juros_compostos(
    valor_investido, rendimento_periodo, aporte_periodo, total_periodos)
