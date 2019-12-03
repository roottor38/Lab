from elasticsearch import Elasticsearch
from bs4 import BeautifulSoup
from time import sleep
import requests, re

host = 'localhost:9200'
es = Elasticsearch([host])

# index 존재 테스트
index_name = 'test-bulk'
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
  "mappings" : {"properties" :
                {"title" : {"type" : "text", "analyzer" : "korean"},
                    "body" : {"type" : "text", "analyzer" : "korean"}
                }}
}
es.indices.create(index=index_name, body=body)

body = []
url = "http://www.inven.co.kr/board/lineagem/5019"
community = "인벤"
for i, el in enumerate(map(lambda el : el['href'], BeautifulSoup(requests.get(url).text, "lxml").select(".tr a"))):    
    print(el)
    sleep(0.3)
    body.append({"create" : {"_index" : index_name, "_id" : i+1}})
    soup = BeautifulSoup(requests.get(el).text, "lxml")
    body.append({"url" : el,
                 "community" : community,
                 "title" : soup.select_one(".articleTitle").text.strip(),
                 "time" : soup.select_one(".articleDate").text.strip(),
                 "body" : re.sub(r'\xa0', "", soup.select_one("#powerbbsContent").text.strip()),
                 "hits" : int(re.search(r'[0-9,]+', soup.select_one(".articleHit").text).group().replace(",", "")),
                 "commments" : int(re.search(r'[0-9,]+', soup.select_one(".articleBottomMenu").text).group().replace(",", "")),
                 "recommand" : int(re.search(r'[0-9,]+', soup.select_one(".reqnum").text).group().replace(",", ""))
                 })
'''
{
   title : 아 기사 너무 쎄요, (not null)
   time : 2019-12-02, (not null)
   class : 기사, ?? 별도로 처리하는 것보다는 search 문에 녹여내는 것이 좋을 듯
   본문 : 너무쌔요 하향좀 (not null)
   조회수 : 1
   커뮤니티 : 인벤 (not null)
   별점 : 1
   추천 :    
   서버 : 
   댓글 : [] ?? 댓글 셀리니움으로 긁어야함
}
'''
# print(body)

es.bulk(index=index_name, body=body)
'''
{ "index" : { "_index" : "test", "_id" : "1" } }
{ "field1" : "value1" }
{ "delete" : { "_index" : "test", "_id" : "2" } }
{ "create" : { "_index" : "test", "_id" : "3" } }
{ "field1" : "value3" }
{ "update" : {"_id" : "1", "_index" : "test"} }
{ "doc" : {"field2" : "value2"} }
'''
es.indices.refresh(index=index_name) # refresh 필수!

res = es.search(index=index_name, body={"query": {"match_all": {}}})["hits"]["hits"] # match_all
print("# match all")
print(res)