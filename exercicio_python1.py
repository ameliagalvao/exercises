"""
Em um concurso de fantasias, os jurados precisam digitar os nomes dos 5 participantes e suas respectivas
notas, variando de 0 até 10. Crie uma função que leia os nomes dos participantes e, ao final, apresente
apenas o nome e a nota do vencedor.

Fluxo de exceção: 
O programa deve verificar se a nota da pessoa é maior ou igual a zero e menor ou igual a dez.
"""


def percentual_comprometido(renda_mensal, gastos_moradia, gastos_educacao, gastos_transporte):
    percentual_gastos_moradia = round((gastos_moradia / renda_mensal) * 100, 2)
    percentual_gastos_educacao = round(
        (gastos_educacao / renda_mensal) * 100, 2)
    percentual_gastos_transporte = round(
        (gastos_transporte / renda_mensal) * 100, 2)

    maximo_moradia = 0.3 * renda_mensal
    maximo_educacao = 0.2 * renda_mensal
    maximo_transporte = 0.15 * renda_mensal

    print(
        f'Seus gastos totais com moradia comprometem {percentual_gastos_moradia}% da sua renda total.')
    if percentual_gastos_moradia > 30:
        print(
            f'O máximo recomendado é 30%. Portanto, idealmente, o máximo de sua renda comprometida com moradia deveria ser de R$ {maximo_moradia}.')
    else:
        print('O máximo recomendado é 30%. Seus gastos estão dentro da margem recomendada')
    print(
        f'Seus gastos totais com educação comprometem {percentual_gastos_educacao}% da sua renda total.')
    if percentual_gastos_educacao > 20:
        print(
            f'O máximo recomendado é 20%.  Portanto, idealmente, o máximo de sua renda comprometida com educação deveria ser de R$ {maximo_educacao}.')
    else:
        print('O máximo recomendado é 20%. Seus gastos estão dentro da margem recomendada')
    print(
        f'Seus gastos totais com transporte comprometem {percentual_gastos_transporte}% da sua renda total.')
    if percentual_gastos_transporte > 15:
        print(
            f'O máximo recomendado é 15%. Portanto, idealmente, o máximo de sua renda comprometida com transporte deveria ser de R$ {maximo_transporte}.')
    else:
        print('O máximo recomendado é 15%. Seus gastos estão dentro da margem recomendada')


renda_mensal = float(
    str(input('Insira a renda mensal em R$: ')).replace(',', '.'))
gastos_moradia = float(
    str(input('Insira os gastos mensais com moradia em R$: ')).replace(',', '.'))
gastos_educacao = float(
    str(input('Insira os gastos mensais com educação em R$: ')).replace(',', '.'))
gastos_transporte = float(
    str(input('Insira os gastos mensais com transporte em R$: ')).replace(',', '.'))

percentual_comprometido(renda_mensal, gastos_moradia,
                        gastos_educacao, gastos_transporte)
