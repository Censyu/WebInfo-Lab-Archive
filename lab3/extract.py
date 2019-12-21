fo = open('data_main.csv', 'w', encoding='utf-8')
fo.write('user,item,rating\n')
with open('data/train.txt', 'r', encoding='utf-8') as f:
    for line in f:
        data = line.split(',')
        fo.write(','.join(data[0:3]) + '\n')

fo.close()