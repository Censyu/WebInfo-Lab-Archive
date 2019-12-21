import os
import pandas as pd
from surprise import SVD
from surprise import KNNWithMeans
from surprise import Dataset
from surprise import Reader
from surprise import dump
from surprise.model_selection import train_test_split
from surprise.model_selection import cross_validate
from surprise import accuracy
from surprise.model_selection import KFold

# df = pd.read_csv('data_main.csv', encoding='utf-8')
# reader = Reader(rating_scale=(0, 5))
# data = Dataset.load_from_df(df, reader)

# # test dataset
# # data = Dataset.load_builtin('ml-100k')

# print('Reading finished!')

# kf = KFold(n_splits=3)
# # algo = SVD()
# algo = KNNWithMeans(k=40, min_k=1, verbose=True)

# count = 0
# for trainset, testset in kf.split(data):
#     count = count + 1
#     print('Training round: ' + str(count))
#     # 训练并测试算法
#     algo.fit(trainset)
#     predictions = algo.test(testset)

#     # 计算并打印 RMSE（均方根误差，Root Mean Squared Error）
#     accuracy.rmse(predictions, verbose=True)

# 保存模型
# dump.dump('saved_model_knnm.model', algo=algo, verbose=1)
algo = dump.load('saved_model.model')[1]

# 生成结果
fo = open('submission.txt', 'w', encoding='utf-8')
with open('data/test.txt', 'r', encoding='utf-8') as f:
    c=0
    for line in f:
        row = line.split(',')
        fo.write(str(algo.predict(int(row[0]), int(row[1])).est) + '\n')
fo.close()
