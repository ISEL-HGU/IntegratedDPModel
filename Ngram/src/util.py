START = "<s>"
END = "</s>"
# import javalang

# def read_and_tokenize_source_code(filename):
#     with open(filename, "r") as file:
#         all_lines = file.readlines()
#     token_list = []
#     return_tokens = [] 
#     for line in all_lines:
#         # print(line)
#         tokens = list(javalang.tokenizer.tokenize(line))
#         for token in tokens:
#             token_list.append(token.value)
#         # print(token_list)
#         return_tokens.append(token_list)
#     return return_tokens
    
def tokenize_data(filename):
    with open(filename, "r") as file:
        all_lines = file.readlines()

    return_list = [] 
    for line in all_lines:
        return_list.append(line.split())
    return return_list