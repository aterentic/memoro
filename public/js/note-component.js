var NoteItem  = React.createClass({
    render: function() {
	function nop() {
	    return false;
	}
	
	function render_checkbox(props) {
	    return (
		<input type="checkbox" checked={props.checked==="true"} onChange={nop} />
	    );
	}
	
	return (
	    <li className="note-item">
		{render_checkbox(this.props)}
	        {this.props.text}
	    </li>
	);
    }
});

var NoteItemForm = React.createClass({
    handleKeyup: function(e) {
	e.preventDefault();
	if (e.keyCode == 13) {
            React.findDOMNode(this.refs.form).submit();
	}
    },
    handleSubmit: function(e) {
	e.preventDefault();
	var text = React.findDOMNode(this.refs.item).value.trim();
	if (!text) {
	    return;
	}
	this.props.onItemSubmit({text: text});
	React.findDOMNode(this.refs.item).value = '';
    },
    render: function() {
	return (
		<form className="item form" ref="form" onSubmit={this.handleSubmit}>
		    <input type="text" placeholder="New note item..." ref="item" onKeyup={this.handleKeyup} />
		</form>
	);
  }
});

var Note = React.createClass({
    noteUrl: function() {
	return "notes/" + location.queryParams["note"];
    },
    itemsUrl: function() {
	return "notes/" + location.queryParams["note"] + "/items";
    },
    handleItemSubmit: function(data) {
	// TODO For some reason, done is not called.
	$.ajax({
	    url:this.itemsUrl(),
	    type: "POST",
	    data: JSON.stringify({
		"text" : data.text,
		"note" : new Number(this.state.data.id),
		"priority": 1,
		"checked?": false
	    }),
	    contentType:"application/json; charset=utf-8",
	    dataType:"json"
	}).always(function(e) {
	    $.get(this.noteUrl()).done(function(data) {
		this.setState({data: data});
	    }.bind(this));	    
	}.bind(this));
    },
    getInitialState: function() {
	return {data: {items: []}};
    },
    componentDidMount: function () {
	$.get(this.noteUrl()).done(function(data) {
	    this.setState({data: data});
	}.bind(this));
    },
    render: function() {
	function render_item(item) {
	    return (
		    <NoteItem text={item["text"]} checked={item["checked?"]} key={item["id"]}/>
	    );
	}
	return (
	    <ul className="note">
		{this.state.data.items.map(render_item)}
	        <NoteItemForm onItemSubmit={this.handleItemSubmit} />
	    </ul>
	);
    }
});

React.render(
    <Note/>,
    document.getElementById('content')
);

