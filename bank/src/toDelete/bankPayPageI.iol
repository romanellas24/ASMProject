type PayTokenPageRequest: void {
    .token: string
}

interface HttpInterface {
RequestResponse:
    payForm(PayTokenPageRequest)(string)
}