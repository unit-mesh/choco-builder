from sentence_transformers import SentenceTransformer

# Download model
model = SentenceTransformer('paraphrase-MiniLM-L6-v2')

# The sentences we'd like to encode
sentences = ['Python is an interpreted high-level general-purpose programming language.',
             'Python is dynamically-typed and garbage-collected.',
             'The quick brown fox jumps over the lazy dog.']

# Get embeddings of sentences
embeddings = model.encode(sentences)

# Print the embeddings
for sentence, embedding in zip(sentences, embeddings):
    print("Sentence:", sentence)
    print("Embedding:", embedding.__len__())
    print("")
