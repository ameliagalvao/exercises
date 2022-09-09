"""
Em um concurso de fantasias, os jurados precisam digitar os nomes dos 5 participantes e suas
respectivas notas, variando de 0 até 10. Crie uma função que leia os nomes dos participantes e,
ao final, apresente apenas o nome e a nota do vencedor.

Fluxo de exceção: O programa deve verificar se a nota da pessoa é maior ou igual a zero e
menor ou igual a dez.
"""


def calcular_vencedor():
    participantes = []
    notas = []

    """ Loop para checar se foi digitado algum caracter e só passar
    para o próximo quando digitado algum."""
    for n in range(5):
        participantes.append(
            input(f'Insira o nome do {n + 1}º participante: '))
        while len(participantes[n]) == 0:
            participantes[n] = input(
                f'Insira o nome do {n + 1}º participante: ')

    """Loop para checar se a nota digitada está entre 0 e 10 e, se não,
    solicitar que seja informada uma nota dentro do intervalo. Só sai
    do loop com a condição preenchida."""
    for n in range(5):
        notas.append(float(input(f'Insira a nota do {n + 1}º participante: ')))
        while notas[n] < 0 or notas[n] > 10:
            print('Por favor, insira uma nota de 0 a 10.')
            notas[n] = float(
                input(f'Insira a nota do {n + 1}º participante: '))
            if notas[n] >= 0 and notas[n] <= 10:
                break

    """ Loop para inserir os valores das listas notas e participantes num
    dicionário, em que participantes serão as chaves e notas os valores
    correspondentes. """
    concurso_fantasia = {}
    for n in range(5):
        concurso_fantasia[f"{participantes[n]}"] = notas[n]

    """ Criação de uma tupla com o maior valor do dicionário, sendo o primeiro
    item a chave correspondente ao maior valor e o segundo o valor em si. """
    vencedor = (max(concurso_fantasia, key=concurso_fantasia.get),
                max(concurso_fantasia.values()))

    return f'O vencedor foi {str(vencedor[0]).title()} com nota {vencedor[1]}'


print(calcular_vencedor())
