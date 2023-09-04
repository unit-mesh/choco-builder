export const runtime = 'edge'

export async function POST(req: Request) {
  console.log(req.body);

  return new Promise((resolve, reject) => {
    resolve(new Response('Hello from the API!'))
  })
}
