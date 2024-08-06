package response

type Standard struct {
	StatusCode int
	Message    string
	Data       interface{}
}
