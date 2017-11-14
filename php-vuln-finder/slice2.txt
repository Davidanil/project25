{
  "kind": "program",
  "children": [
    {
      "kind": "assign",
      "operator": "=",
      "left": {
        "kind": "variable",
        "name": "u",
        "byref": false,
        "curly": false
      },
      "right": {
        "kind": "offsetlookup",
        "what": {
          "kind": "variable",
          "name": "_GET",
          "byref": false,
          "curly": false
        },
        "offset": {
          "kind": "string",
          "value": "username",
          "isDoubleQuote": false
        }
      }
    },
    {
      "kind": "assign",
      "operator": "=",
      "left": {
        "kind": "variable",
        "name": "q",
        "byref": false,
        "curly": false
      },
      "right": {
        "kind": "bin",
        "type": ".",
        "left": {
          "kind": "bin",
          "type": ".",
          "left": {
            "kind": "string",
            "value": "SELECT pass FROM users WHERE user='",
            "isDoubleQuote": true
          },
          "right": {
            "kind": "variable",
            "name": "u",
            "byref": false,
            "curly": false
          }
        },
        "right": {
          "kind": "string",
          "value": "'",
          "isDoubleQuote": true
        }
      }
    },
    {
      "kind": "assign",
      "operator": "=",
      "left": {
        "kind": "variable",
        "name": "query",
        "byref": false,
        "curly": false
      },
      "right": {
        "kind": "call",
        "what": {
          "kind": "identifier",
          "resolution": "uqn",
          "name": "mysql_query"
        },
        "arguments": [
          {
            "kind": "variable",
            "name": "q",
            "byref": false,
            "curly": false
          }
        ]
      }
    }
  ],
  "errors": []
}