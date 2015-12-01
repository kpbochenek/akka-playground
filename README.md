
# Send correct request to calculator (result is 80)
curl -X POST -H "Content-Type:application/json" -d @jsons/multiply.json -v localhost:8000/calculate

# Send correct request to calculator (result is 5)
curl -X POST -H "Content-Type:application/json" -d @jsons/div_ok.json -v localhost:8000/calculate

# Send request to divide by 0 (response without 'value' field)
curl -X POST -H "Content-Type:application/json" -d @jsons/div_0.json -v localhost:8000/calculate

# Send request to do unknown operation (400 response with message about malformed "x")
curl -X POST -H "Content-Type:application/json" -d @jsons/wrong.json -v localhost:8000/calculate
