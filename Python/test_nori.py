from elasticsearch import Elasticsearch
from datetime import datetime

host = 'localhost:9200'
es = Elasticsearch([host])

# index 존재 테스트
index_name = 'test-index'
print("# " + index_name + "이 있나?")
print(es.indices.exists(index_name))
if es.indices.exists(index_name):  # 해당 인덱스가 존재하면
    es.indices.delete(index_name)

# nori 플러그인을 사용하기 위한 세팅
settings = {
    "index": {
      "analysis": {
        "tokenizer": {
          "nori_tokenizer_mixed": {
            "type": "nori_tokenizer",
            "decompound_mode": "mixed"
          }
        },
        "analyzer": {
          "korean": {
            "type": "custom",
            "tokenizer": "nori_tokenizer_mixed",
            "filter": [
              "nori_readingform",
              "lowercase",
              "nori_part_of_speech_basic"
            ]
          }
        },
        "filter": {
          "nori_part_of_speech_basic": {
            "type": "nori_part_of_speech",
            "stoptags": ["E", "IC", "J",
                         "MAG", "MAJ", "MM",
                         "SP", "SSC", "SSO", "SC", "SE",
                         "XPN", "XSA", "XSN", "XSV",
                         "UNA", "NA", "VSV"]
          }
        }
      }
    }
  }
# nori 플러그인을 사용하기 위한 매핑 작업까지 완료
body = {
  "settings": settings,
  "mappings" : {"properties" : {"message" : {"type" : "text", "analyzer" : "korean"}}}
}

id = 0
es.indices.create(index_name, body=body)
doc = {"name": "패닉", "message" : "좁은 욕조 속에 몸을 뉘었을 때 작은 달팽이 한 마리가 내게로 다가와 작은 목소리로 속삭여줬어", "time_stamp": datetime.now()}
id += 1
es.create(index=index_name, id=id, body=doc)
doc = {"name": "패닉", "message" : "해는 높이 떠서 나를 찌르는데 작은 달팽이 한 마리가 어느새 다가와 내게 인사하고 노랠 흥얼거렸어", "time_stamp": datetime.now()}
id += 1
es.create(index=index_name, id=id, body=doc)
doc = {"name": "카니발", "message" : "난 난 꿈이 있었죠 버려지고 찢겨 남루하여도 내 가슴 깊숙히 보물과 같이 간직했던 꿈", "time_stamp": datetime.now()}
id += 1
es.create(index=index_name, id=id, body=doc)

es.indices.refresh(index="test-index") # refresh 필수!

res1 = es.search(index=index_name, body={"query": {"match_all": {}}})["hits"]["hits"] # match_all
print("# match all")
print(res1)
res2 = es.search(index=index_name, body={"query": {"term": {"message" : "나"}}})["hits"]["hits"] # term query
print("# term query")
print(res2)
query = {
    "query": {
      "bool": {
        "must": [
          {"match": {"message": "나"}},
          {"match": {"name": "패닉"}}
        ]
      }
  }
}
print("# bool query")
res3 = es.search(index=index_name, body=query)["hits"]["hits"] # bool query (다중 조건)
print(res3)