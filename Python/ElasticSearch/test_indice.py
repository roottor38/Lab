from elasticsearch import Elasticsearch
from datetime import datetime

host = 'localhost:9200'
es = Elasticsearch([host])

# index 존재 테스트
index_name = 'test-index'
print(es.indices.exists(index_name))
if es.indices.exists(index_name): # 해당 인덱스가 존재하면
    es.indices.delete(index_name)
    # print("삭제 진행됨")
es.indices.create(index_name) # 새롭게 만들어준다
doc = {"name" : "young-in", "age" : 27, "time_stamp" : datetime.now()}
es.create(index=index_name, id=1, body=doc)