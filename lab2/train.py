from kashgari.embeddings import BERTEmbedding
from kashgari.tasks.labeling import BiLSTM_CRF_Model
from kashgari.utils import load_model

train_path = 'data/1train.txt'
valid_path = 'data/2valid.txt'
test_path = 'data/1test.txt'

model_save_path = 'trained_model/saved_model1'

def parse_data(file_path):
    data_x, data_y = [], []
    with open(file_path, 'r', encoding='utf-8') as f:
        lines = f.read().splitlines()

        x, y = [], []
        for line in lines:
            rows = line.split(' ')
            if len(rows) == 1:
                data_x.append(x)
                data_y.append(y)
                x = []
                y = []
            else:
                x.append(rows[0])
                y.append(rows[1])
    return data_x, data_y

train_x, train_y = parse_data(train_path)
validate_x, validate_y = parse_data(valid_path)

# 加载 Bert 预训练模型
# embedding = BERTEmbedding('chinese_L-12_H-768_A-12', 128)

# 使用 BiLSTM - CRF 模型
# model = BiLSTM_CRF_Model(embedding)
model = BiLSTM_CRF_Model()

# 训练 (e, b) = (5+, 30), (10, 50)， (3, 32)*
epochs = 3
batch_size = 32
model.fit(train_x,
        train_y,
        x_validate = validate_x,
        y_validate = validate_y,
        epochs = epochs,
        batch_size = batch_size)
        
# 保存模型
model.save(model_save_path + "[epochs=" + str(epochs) + ", bath_size=" + str(batch_size) + "]")
