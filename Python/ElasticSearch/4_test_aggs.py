from elasticsearch import Elasticsearch
from bs4 import BeautifulSoup
from time import sleep
import requests, re, csv
def save_csv(name, data):
    print("# CSV 파일 저장")
    with open(name, "w", encoding="utf-8", newline="") as f:
        # csvwriter = csv.writer(f)
        # for row in data:
        #     csvwriter.writerow(row)
        csv.writer(f).writerows(data)
def scraping(url, index_name, es=None):
    if es is None : es = Elasticsearch()
    if es.indices.exists(index_name): es.indices.delete(index_name)
        
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
    body = {
    "settings": settings,
    "mappings" : {"properties" :
                    {"title" : {"type" : "text", "analyzer" : "korean"},
                        "body" : {"type" : "text", "analyzer" : "korean"}
                    }}
    }
    es.indices.create(index=index_name, body=body)

    body = []
    community = "인벤"
    print("# 크롤링 시작")
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
    es.bulk(index=index_name, body=body)
    print("# 벌크 저장 완료")
    es.indices.refresh(index=index_name)
    print("# 엘라스틱 서치 반영 완료")
def read_aggs(index_name, es=None):
    if es is None : es = Elasticsearch()
    if not es.indices.exists(index_name): return None
    '''
    GET test-okt/_search
    {
    "query": {"match_all": {}},
    "size": 0,
    "aggs": {
        "hits": {
        "stats": {
            "field": "hits"
        }
        }
    '''
    body = {"query": {"match_all": {}}, "size": 0,
            "aggs": {"hits_aggs": {"stats": {"field": "hits"}}}}
    return es.search(index_name, body=body)

# scraping("http://www.inven.co.kr/board/lineagem/5019", "test-okt")
save_csv("bbs_count.csv", read_aggs("test-okt")['aggregations']["hits_aggs"].items())